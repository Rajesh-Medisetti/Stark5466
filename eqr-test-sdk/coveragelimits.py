"""
Test that the code is passing minimum coverage limits.

Coverage limits are updated whenever release is merged in this spreadsheet -

https://docs.google.com/spreadsheets/d/1vK3YIJdkM89IfzjRQcutA-14CNvX0ux9XuuDzto0ovM/edit?usp=sharing

"""
import json
import os
import sys
from csv import reader

import arrow
import gspread
from oauth2client.service_account import ServiceAccountCredentials

COVERAGE_LIMITS_SPREADSHEET_SECRET = os.environ.get(
    'COVERAGE_LIMITS_SPREADSHEET_SECRET',
)
COVERAGE_LIMITS_SPREADSHEET_WORKSHEET = os.environ.get(
    'COVERAGE_LIMITS_SPREADSHEET_WORKSHEET',
)
GIT_BRANCH = os.environ.get('GIT_BRANCH')

scope = (
    'https://spreadsheets.google.com/feeds',
    'https://www.googleapis.com/auth/drive',
)

secret = json.loads(COVERAGE_LIMITS_SPREADSHEET_SECRET)
secret['private_key'] = secret['private_key'].replace('\\n', '\n')

creds = ServiceAccountCredentials.from_json_keyfile_dict(
    secret, scope,
)
client = gspread.authorize(creds)
sheet = client.open('Coverage Limits').worksheet(
    COVERAGE_LIMITS_SPREADSHEET_WORKSHEET,
)

values = sheet.row_values(2)

coverage_date, code_coverage_threshold, branch_coverage_threshold = (
    arrow.get(values[0], 'DD-MMM-YYYY'), float(values[1]), float(values[2]),
)

print('Last Coverage Date:', values[0])
print('Coverage Coverage Threshold:', code_coverage_threshold)
print('Branch Coverage Threshold:', branch_coverage_threshold)

data_file = 'target/site/jacoco/jacoco.csv'

if not os.path.isfile(data_file):
    print(
        'jacoco coverage file does not exist. Please run "mvn clean test" and '
        '"mvn jacoco:report"',
    )
    sys.exit(1)


modified_time = arrow.get(os.path.getmtime(data_file)).to('Asia/Kolkata')

today = arrow.now('Asia/Kolkata')

if modified_time.date() < today.date():
    print('Please run tests today and then run coveragelimits.py')
    sys.exit(1)

total_statements = 0
untested_statements = 0
total_branches = 0
taken_branches = 0

with open('target/site/jacoco/jacoco.csv', 'r') as read_obj:
    # pass the file object to reader() to get the reader object
    csv_reader = reader(read_obj)
    # Get all rows of csv from csv_reader object as list of tuples
    list_of_tuples = list(map(tuple, csv_reader))
    # display all rows of csv
    coverage_data = list_of_tuples[1:]

for line in coverage_data:
    total_statements += int(line[3]) + int(line[4])
    untested_statements += int(line[3])

    total_branches += int(line[5]) + int(line[6])
    taken_branches += int(line[5])

tested_statements = total_statements - untested_statements

if total_statements > 0:
    code_coverage_ratio = tested_statements / total_statements * 100
else:
    code_coverage_ratio = 0.0
if total_branches > 0:
    branch_coverage_ratio = taken_branches / total_branches * 100
else:
    branch_coverage_ratio = 0.0

print('---------------------------------------')
print('Total Statements : ', total_statements)
print('Tested Statements : ', tested_statements)
print('Code Coverage Ratio : %.2f' % code_coverage_ratio)
print('=======================================')

if code_coverage_ratio < code_coverage_threshold - 0.5:
    print(
        'Code Coverage Ratio below threshold of %.2f' %
        code_coverage_threshold,
    )
    sys.exit(1)

print('---------------------------------------')
print('Total Branch Paths : ', total_branches)
print('Taken Branches : ', taken_branches)
print('Branch Coverage Ratio : %.2f' % branch_coverage_ratio)
print('=======================================')

if branch_coverage_ratio < branch_coverage_threshold - 0.5:
    print(
        'Branch Coverage Ratio below threshold of %.2f' %
        branch_coverage_threshold,
    )
    sys.exit(1)

if GIT_BRANCH != 'release':
    sys.exit(0)

update_code_coverage_ratio = code_coverage_ratio - 0.5
if update_code_coverage_ratio < code_coverage_threshold:
    update_code_coverage_ratio = code_coverage_threshold

update_branch_coverage_ratio = branch_coverage_ratio - 0.5
if update_branch_coverage_ratio < branch_coverage_threshold:
    update_branch_coverage_ratio = branch_coverage_threshold

sheet.insert_row(
    [
        today.format('DD-MMM-YYYY'),
        '{0:.2f}'.format(update_code_coverage_ratio),
        '{0:.2f}'.format(update_branch_coverage_ratio),
    ],
    2,
)
