# VAPIr (Vail API)

## Description
VAPIr is a command line script for both linux and Windows (.bat) environments to allow automated administration a Spectra Logic Vail storage solution. This is a work in progress that is being expanded as needed. The initial goal of this program is to allow the filtering of users by their account and/or if the account is activated in the Vail Sphere. This can also be used to see what accounts buckets belong to and filter buckets by account. If any features are desired, please log them in the Issues as enhancement requests.

Vail does not store a human readable name for the account. This code substitutes the username specified when attaching the account to the Vail Sphere as the name of the account. For the AWS account housing the Sphere, "Sphere" will be listed as the account name.

This code uses Google's Gson for parsing JSON API responses and requires JDK 14 or higher to run.

## Using
vapir is a command line tool. After decompressing the tar or zip file, nagivate to the bin directory to find the vapir shell or batch script. Windows users will use the batch script (.bat). Linux/Mac OS users will use the shell script to execute commands.

### Samples
The following samples demonstrate a few of the commands for vapir. Capitalized words represent variable values that should be edited to reflect the execution environment. For example, IP_ADDRESS must be replaced with the IP address of the Spectra Vail Sphere (management server), such as 10.10.10.15. 

Some of the flags can be abbreviated to their initial letter. This can be seen in the below examples where both --command and -c work, --endpoint and -e work, and --username and -u work for parsing the variable.

#### Get a list of all accounts (Linux/Mac OS)
`./vapir --ignore-ssl --endpoint IP_ADDRESS --username USERNAME --password PASSWORD --command list-accounts`

#### List All Active (Windows)
`./vapir.bat --ignore-ssl --endpoint IP_ADDRESS --username USERNAME --password PASSWORD -c list-users --active-only`

#### List All Users Belonging To Account 0123456789 in CSV Format(Linux/Mac OS)
`./vapir --ignore-ssl -e IP_ADDRESS -u USERNAME -p PASSWORD -c list-users --account 0123456789 --output-format csv`

#### List All Buckets(Windows)
`./vapir.bat --ignore-ssl -e IP_ADDRESS -u USERNAME -p PASSWORD -c list-buckets --account all`

### Options
--account&emsp;&emsp;Filter list by owning account name or account id. Use all to list all items in response.  
--active-only&emsp;Filter the user list to only display active users.  
--command(-c)&emsp;The command to be executed. Commands can be listed with -c help, -c help-basic, or -c help-advanced  
--ignore-ssl&emsp;&emsp;Ignore the SSL certificate returned by the server.  
--endpoint(-e)&emsp;&ensp;IP Address of the Stack  
--output-format&emsp;&ensp;The format in which ouput will be displayed. Accepts csv, shell, table, and xml. Defaults to table.  
--password(-p)&emsp;Password  
--username(-u)&emsp;&ensp;Username  

### Commands
--configure-sphere&emsp;Loads a JSON configuration file to configure a sphere.  
--fetch-config&emsp;&emsp;Saves accounts, groups, storage locations, lifecycles, and bucket info in JSON format to --file  
--list-accounts&emsp;List all accounts paired with the Vail system.  
--list-buckets&emsp;List buckets. Use --account [ all | ACCOUNT_NAME | ACCOUNT_ID ] to filter results.  
--list-users&emsp;&ensp;List users. Use --account [ all | ACCOUNT_NAME | ACCOUNT_ID ] and/or --active-only to filter results.  

## Errors
vapir stores logs in the log/ sub-directory for the program. The name of the file is vail_api.log

## Configuration JSON
The configure-sphere command can import a JSON configuration file to automate the configuration of a Vail sphere. The configuration file can specify accounts, groups, storage locations, lifecycles, and buckets. These fields can be defined in any order.

### Accounts
Vail allows for attaching external AWS accounts for handling identity and access management across a large organization. This allows multiple organizations to share a single Vail sphere while maintaining AWS cross-account permissions. The configuration file has a parameter, accounts, for adding these accounts.

#### AWS Accounts
##### Minimum Required Fields
username&emsp;&emsp;The username associated with the account. This is functionally the name of the account.  
roleArn&emsp;&emsp;&emsp;The AWS ARN associated with the account. This should be in the format of arn:aws:iam::ACCOUNT_NUMBER:role/ROLE_NAME  
email&emsp;&emsp;&emsp;&emsp;The email associated with the administrator of that account.

##### Additional Fields
externalId&emsp;&emsp;The external ID, if defined, associated with the role ARN.

### Groups
Groups specified in the configuration JSON are internal to Vail and do not exist outside of the sphere in any of the attached AWS accounts. Groups do not have any innate permissions and permissions are applied to groups at the bucket policy level. This parameter, groups, is a pair of the group name and the account name.

##### Required Fields
name&emsp;&emsp;&emsp;&emsp;The name of the group.  
account_name&emsp;The username of the account the group is associated with.  

### Storage Locations
The configuration file has a parameter, storage, to specify BlackPearl and cloud storage locations. Any storage location requires credentials to be added to the sphere. For the BlackPearl storage, the script will prompt you for the username and password. For AWS storage locations, the access key and secret key will need to be included in the JSON.

#### BlackPearl Bucket Storage
Blackpearl storage locations require only a few fields of information in order to be added to a Vail sphere. At the current version of the script, BlackPearl credentials are not stored in the JSON. A prompt will ask for the BlackPearl's username and password while configuring the storage location for the BlackPearl. This is only required for the first storage location associated with the BlackPearl. Credentials will be cached for subsequent storage locations. All fields are strings unless otherwise noted.

##### Minimum Required Fields
name&emsp;&emsp;The name of the storage location in Vail. This does not have to be the same as the bucket name.  
bucket&emsp;&emsp;The bucket name in the BlackPearl Nearline Gateway  
type&emsp;&emsp;The storage type. In this case it must be bp.  
endpoint&emsp;&emsp;The hostname of the BlackPearl  
cautionThreshold&emsp;What utilization threshold should generate a caution message. INTEGER  
warningThreshold&emsp;What utilization threshold should generate a warning message. INTEGER  

##### Optional Parameters
link&emsp;&emsp;A BlackPearl bucket to link to this storage location as read only.

### Lifecycles
Lifecycles are the storage rules applied to Vail buckets. They include where the date is stored and for what duration.

##### Minimum Required Fields
name&emsp;&emsp;&emsp;The name of the lifecycle rule.  
rules&emsp;&emsp;&emsp;ARRAY of lifecycle rules.

###### Optional Parameters
description&emsp;&emsp;A description of the lifecycle.  
markers&emsp;&emsp;&emsp;BOOLEAN. Remove delete markers when they are the only remaining version of an object.
uploads&emsp;&emsp;&emsp;INTEGER. The number of days a multipart upload should stay active until they are timed-out and deleted.

#### Lifecycle Rules
Lifecycles are composed of one or more rules dictating how objects will be stored. These fields are required to be listed in an array of the rules parameter of the lifecycle.

##### Minimum Required Fields
name&emsp;&emsp;&emsp;The name of the specific rule.
type&emsp;&emsp;&emsp;[clone | move | expire] The type of rule to be applied. *Expire rules aren't supported until scheduling is added.*
destinations&emsp;Storage locations stored as a sub-array titled storage. The storage array should be a list of names of the storage locations.  

##### Optional Parameters
count&emsp;&emsp;INTEGER. Set under the destinations parameter along with storage to specify the number of storage locations as an ANY X of Y rule. The default is all if not specified.

##### Sample Lifecycle Rule
```
"lifecycles": [
		{
			"name": "scripted-test-1",
			"rules": [
				{
					"name": "script-clone",
					"type": "clone",
					"destinations": {
						"storage": [ "s3-share" ]
					}
				}
			]
		}
	]
```

### Buckets
Buckets are the primary interface between the Vail system and act as a group of data for the application of storage rules. Due to some limits with information present in the API calls, this script does not support object locking. Any buckets created with object locking enabled will have object locking disabled by the script and a message will be presented informing the user to configure object locking from the UI after bucket creation. While ACLs can be applied with this script, they are a deprecated parameter in AWS and Spectra recommends against using them.

##### Minimum Required Fields
name&emsp;&emsp;Bucket name  
owner&emsp;&emsp;The account name of the account that should own the bucket. Sphere (all lower-case) can be used to reference the local account or the username of any account should be used.

##### Options Fields
blockPublicPolicy&emsp;BOOLEAN. Prevent applying new bucket policies that allow public access to the bucket.  
compress&emsp;&emsp;BOOLEAN. Enable compression.  
control&emsp;&emsp;[BucketOwnerEnforced | BucketOwnerPreferred | ObjectWriter] Default object ownership. Spectra recommends setting BucketOwnerEnforced. If not specified, it defaults to ObjectWriter.  
encrypt&emsp;&emsp;BOOLEAN. Enable encryption.  
lifecycle&emsp;The name of the lifecycle rule to be attached to the bucket.  
locking&emsp;&emsp;BOOLEAN. Enabled object locking *NOT SUPPORTED. WILL DISABLE*  
restore&emsp;&emsp;BOOLEAN. Enabled automatic restores.  
restrictPublicBuckets&emsp; BOOLEAN. Ignore policies that allow public or cross-account access to buckets.  
versioning&emsp;&emsp;[enabled | disabled] Whether or not the bucket will version objects.
