# spring-birt-example
An Example integration of spring and birt

Example maven project using spring mvc version 4.2 and eclipse birt version 4.4
Refer following links for more information. This project is a custom html ui implementation of reports. containing a dashboard and full report

http://www.eclipse.org/birt/documentation/

https://spring.io/docs/reference

https://spring.io/blog/2012/01/30/spring-framework-birt

https://github.com/joshlong/spring-birt

I used a sample mysql db available in following link to generate reports

https://dev.mysql.com/doc/sakila/en/

##Reports

All report files are found in /src/main/webapp/WEB-INF/reports folder. files ends with '-thumb.rptdesign' is used generate dashboard. while the other one used to generate main report. Currently this application supports only integer and string parameters.

Please update the report files found in /src/main/webapp/WEB-INF/reports/ using eclipse birt ui to use different database. Database credentials are stored in report files, so please update report files to change database password.
