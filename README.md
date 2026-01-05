README.txt

PART 2:

I use a maven: 
when the project is working am going to clean the mevan first to clean the old dependencies 

maven clean --> maven install compile --> maven build

then maven package --> then project is ready to deploy so import to the dedicated env.

NB: if necessary the project run as service and run on server we can write a bash script that help us the service run auto-enabled any time

Question 2 - Linux and bash scripting

2.1 - df -g is to check the overall disk space  as 80% usage


2.2 - /var/log/app/sys.log 

we can grep it by 

tail -f  /var/log/app/sys.log | grep 'Exception'

2.3 - ps -ef | grep legacy-app , the we can kill the process after we get PID by [kill -9 PID]

2.4 - crontab
!#cleanup
#/opt/scripts/cleanup.sh 

var lgDate = date;
crony

ExecStart /opt/scripts/cleanup.sh 



2.5 - we can find the whole by simply - [ls -lrt | grep /var/log/app | grep date] and then the file listed  -- then backup using mv /var/log/app<> /backup/archive

!#backup
 lgDate = 'date'
 if lgDate <=30 
	show 'ls -lrt | grep /var/log/app | grep'lgDate <=30''
 then mv /var/log/app<archived file> /backup/archive
 
 display "archived" || print "archived"

 


Question 3 :


Question 4:

we have to check the three scenarios network, db, infra thing : we can test the connectivity and have info from our testing tool from our response

4.1 - if the connectivity is fine and only for the GET /api/customers endpoints:  

we have to be sure the connectivity is fine by test response of the response code like: latency from our postman or testing tools then

As the endpoint is returning the customers detail we have to do the proper indexing for user_id field in db side and normalization if it needed or LIMIT in our endpoint that return the detail in as a JPA mechanism

Scaling -- pagination is the filtering mechanism of the record to retun data of 20 records at a time we can annotate it or manage it from interface
