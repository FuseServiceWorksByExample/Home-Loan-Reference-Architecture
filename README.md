Fuse Service Works Home Loan Reference Architecture for Beta
============================================================

This Reference Architecture will demonstrate a Fuse Service Works Application and highlight the Switchyard Application with Rules, Business Process and Camel Routes as well as Design Time Governance and Run Time Governance.  A intake service receives message through JMS or SOAP then depending on whether they are a customer or not will automatically or manually evaluate a home loan application for approval or denial.

For more detailed information for the Beta release please review http://www.ossmentor.com/2013/10/fuse-service-works-v6-beta-announced.html.  The wiki will be updated with more detailed instructions the week of Beta launch.

The repository consist of the following folders.  Videos and more detail install/deploy/run instructions will be added but feel free to use as is.

DataVirtualization - Project for the Virtual Database for MySQL and Salesforce to define current customers.

homeloan - Switchyard Application with business process, camel route and rules

dtov-workflows - Design Time governance workflows

soapui - SOAPUI project to send SOAP messages to the Intake service

mysql - sql script to setup mysql locally
