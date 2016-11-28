Notification Service
=====================
Design and present a notification service. The requirements below are just basics, feel free to expand and propose more. Focus on any part of the solution to demonstrate your strengths, no need to do it all.  Do not worry about lack of requirements, be creative, apply your experience.  All documents, descriptions diagrams in English. Please implement part of the solution in Java and share the source code with us preferably in a repo on github or bitbucket. 

We will review it and then brainstorm with you about your proposal face to face. Be reasonably prepared to react to requirement changes. There is probably no "correct" solution, we want to see you look for one. 


Landscape 
=========
Set of applications and services with separate user interfaces built on various technologies.  All using company wide single-sign-on solution to authenticate users. LDAP based user groups are used to simplify authorization and manage users' access and roles. More than 10000 users using some of the systems.


The service functionality 
===========================
Universal notification service capable of distribution of alerts, notifications, warnings, confirmations, simple approval requests etc. 

Multi channel - the events should be distributed using multiple channels - through any of integrated services' web UIs, email, company instant messenger, SMS. 

Any integrated application/system should be able to post an event for distribution and mark it as irrelevant, so that it is no longer displayed to the users 

Events should be targeted both to individual users or to groups or roles. 


Technology limitations
=======================
- Use Java. 
- Whole solution to be deployed internally. 
- Support for (almost) zero-downtime upgrades.
- Use any opensource tools/libraries/frameworks
