## Getting started
We use terraform cloud for applying kubernetes manifest files. Here are the steps
* Create a feature branch, push your changes and open a pull request.
* Terraform cloud will update the pull request with the output of `terraform plan` command. If there is an error, it will update the PR accordingly.
* Merge the PR into master if the output is what you expected.
* Terraform cloud will now create a plan and wait for your approval. Verify that the plan output is expected.
* Approving the plan will trigger the infrastructure change. At the end of run, it will print the output and update the state file.

## Requirements

| Name | Version |
|------|---------|
| terraform | ~> 0.14 |
| aws | ~> 3 |
| github | ~> 4 |
| kubernetes | ~> 2 |
| local | ~> 2 |
| null | ~> 3 |
| random | ~> 3 |
| template | ~> 2 |

## Providers

| Name | Version |
|------|---------|
| aws | ~> 3 |
| kubernetes | ~> 2 |

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| ENVIRONMENT | required common variables | `any` | n/a | yes |
| JAVA\_APPLICATION\_CPU\_LIMIT | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_CPU\_REQUEST | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_DESIRED\_REPLICAS | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_DOCKER\_IMAGE\_NAME | required java-application variables | `string` | `""` | no |
| JAVA\_APPLICATION\_DOCKER\_IMAGE\_TAG | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_HTTP\_GET\_PATH | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_HTTP\_GET\_PORT | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_INGRESS\_HOSTNAME | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_MAX\_REPLICAS | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_MEMORY\_LIMIT | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_MEMORY\_REQUEST | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_MIN\_REPLICAS | n/a | `string` | `""` | no |
| JAVA\_APPLICATION\_REQUIRED\_APPROVALS | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_CPU\_LIMIT | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_CPU\_REQUEST | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_DESIRED\_REPLICAS | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_DOCKER\_IMAGE\_NAME | required react-application variables | `string` | `""` | no |
| REACT\_APPLICATION\_DOCKER\_IMAGE\_TAG | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_HTTP\_GET\_PATH | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_HTTP\_GET\_PORT | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_INGRESS\_HOSTNAME | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_MAX\_REPLICAS | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_MEMORY\_LIMIT | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_MEMORY\_REQUEST | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_MIN\_REPLICAS | n/a | `string` | `""` | no |
| REACT\_APPLICATION\_REQUIRED\_APPROVALS | n/a | `string` | `""` | no |

## Outputs

No output.
