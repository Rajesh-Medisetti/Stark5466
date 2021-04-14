"""
Test that the code is passing minimum coverage limits.

Coverage limits are updated whenever release is merged in this spreadsheet -

https://docs.google.com/spreadsheets/d/1vK3YIJdkM89IfzjRQcutA-14CNvX0ux9XuuDzto0ovM/edit?usp=sharing

"""
import json
import os
import sys

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

coverage_date = arrow.get(values[0], 'DD-MMM-YYYY')
statement_coverage_threshold = float(values[1])
branch_coverage_threshold = float(values[2])
function_coverage_threshold = float(values[3])
line_coverage_threshold = float(values[4])

print('Last Coverage Date:', values[0])
print('Statement Coverage Threshold:', statement_coverage_threshold)
print('Branch Coverage Threshold:', branch_coverage_threshold)
print('Function Coverage Threshold:', function_coverage_threshold)
print('Line Coverage Threshold:', line_coverage_threshold)

data_file = 'coverage/coverage-summary.json'

if not os.path.isfile(data_file):
    print(
        'coverage/coverage-summary.json does not exist. '
        'Please run tests using '
        '"CI=true yarn test --coverage"',
    )
    sys.exit(0)


modified_time = arrow.get(os.path.getmtime(data_file)).to('Asia/Kolkata')

today = arrow.now('Asia/Kolkata')

if modified_time.date() < today.date():
    print('Please run tests today and then generate coveragelimits.py')
    sys.exit(1)


with open(data_file, 'r+') as handle:
    coverage_summary = json.loads(handle.read())

statement_coverage_ratio = coverage_summary['total']['statements']['pct']
branch_coverage_ratio = coverage_summary['total']['branches']['pct']
function_coverage_ratio = coverage_summary['total']['functions']['pct']
line_coverage_ratio = coverage_summary['total']['lines']['pct']

print('---------------------------------------')
print('Total Statements : ', coverage_summary['total']['statements']['total'])
print(
    'Tested Statements : ',
    coverage_summary['total']['statements']['covered'],
)
print('Statement Coverage Ratio : %.2f' % statement_coverage_ratio)
print('=======================================')

if statement_coverage_ratio < statement_coverage_threshold - 0.5:
    print(
        'Statement Coverage Ratio below threshold of %.2f' %
        statement_coverage_threshold,
    )
    sys.exit(1)

print('---------------------------------------')
print('Total Branches : ', coverage_summary['total']['branches']['total'])
print('Tested Branches : ', coverage_summary['total']['branches']['covered'])
print('Branch Coverage Ratio : %.2f' % branch_coverage_ratio)
print('=======================================')

if branch_coverage_ratio < branch_coverage_threshold - 0.5:
    print(
        'Branch Coverage Ratio below threshold of %.2f' %
        branch_coverage_threshold,
    )
    sys.exit(1)

print('---------------------------------------')
print('Total Functions : ', coverage_summary['total']['functions']['total'])
print('Tested Functions : ', coverage_summary['total']['functions']['covered'])
print('Function Coverage Ratio : %.2f' % function_coverage_ratio)
print('=======================================')

if function_coverage_ratio < function_coverage_threshold - 0.5:
    print(
        'Function Coverage Ratio below threshold of %.2f' %
        function_coverage_threshold,
    )
    sys.exit(1)

print('---------------------------------------')
print('Total Lines : ', coverage_summary['total']['lines']['total'])
print('Tested Lines : ', coverage_summary['total']['lines']['covered'])
print('Line Coverage Ratio : %.2f' % line_coverage_ratio)
print('=======================================')

if line_coverage_ratio < line_coverage_threshold - 0.5:
    print(
        'Line Coverage Ratio below threshold of %.2f' %
        line_coverage_threshold,
    )
    sys.exit(1)

if GIT_BRANCH != 'release':
    sys.exit(0)

statement_coverage_ratio = statement_coverage_ratio - 0.5
if statement_coverage_ratio < statement_coverage_threshold:
    statement_coverage_ratio = statement_coverage_threshold

branch_coverage_ratio = branch_coverage_ratio - 0.5
if branch_coverage_ratio < branch_coverage_threshold:
    branch_coverage_ratio = branch_coverage_threshold

function_coverage_ratio = function_coverage_ratio - 0.5
if function_coverage_ratio < function_coverage_threshold:
    function_coverage_ratio = function_coverage_threshold

line_coverage_ratio = line_coverage_ratio - 0.5
if line_coverage_ratio < line_coverage_threshold:
    line_coverage_ratio = line_coverage_threshold

sheet.insert_row(
    [
        today.format('DD-MMM-YYYY'),
        '{0:.2f}'.format(statement_coverage_ratio),
        '{0:.2f}'.format(branch_coverage_ratio),
        '{0:.2f}'.format(function_coverage_ratio),
        '{0:.2f}'.format(line_coverage_ratio),
    ],
    2,
)
