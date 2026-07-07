# RemindRx User Manual

Welcome to **RemindRx**, your personal medication management and adherence companion. This guide will help you navigate the app and make the most of its features.

---

1	INTRODUCTION

This user manual provides essential information when it comes to the RemindRx application using android applications. It dives into what it is used for, how to use the application, what the functions of the application are used for and error handing of the application.

1.1	Purpose and Scope

The purpose of this manual is to guide users in installing, accessing, and using RemindRx. This user manual covers all of the core system features such as user authentication, medication management, reminders, refill tracking, and adherence logging. The scope includes normal operation of the application and basic troubleshooting. 

1.2	Organization

This manual is organized as follows:

•	Section 1: Introduction and overview

•	Section 2: System capabilities

•	Section 3: Description of system functions

•	Section 4: Operating instructions

•	Section 5: Error handling and help facilities

1.3	Points of Contact

In order to provide assistance please contact the RemindRx Support Team:
•	Jcbrook2@uab.edu
•	jlplayer@uab.edu
•	davisps@uab.edu
•	hbgolden@uab.edu
•	dmlyles@uab.edu

1.4	Project References
    
    1.	Kotlin Documentation. 
    2.	Requirements and Design
    3.	Test Plan
    4.	Room Database Documentation
    5.	Android Developer Documentation
   
1.5	Primary Business Functions
  	
Remind Rx supports the following primary user tasks

    •	Managing medications entered into the database through adding, editing, and deleting
    
    •	Logging medication adherence
    
    •	Monitor refill needs based on dosage and scheduling
    
    •	MVVM: Model-View-ViewModel architecture
    
    •	Room Database: Local storage system used by the application

These functions together help to improve the overall user experience, provide clarity, store user data, and improve overall health outcomes.

1.6	Glossary

    •	Adherence: Taking medications as prescribed
    
    •	Notification: Alert sent to remind users to take medication
    
    •	Refill Tracking: Monitoring remaining medication supply
    
    •	Room Database: Local storage system used by the app  

2	SYSTEM CAPABILITIES

2.1	Purpose

Remind Rx is designed to improve medication adherence by providing structured tracking, reminders, and logging feature within a mobile application.

2.2	General Description

Feature	Description

    User Authentication: Secure login and account management
    
    Medication Management: Create, edit, and delete prescriptions
    
    Reminder Notifications: Scheduled alerts for medication times
    
    Adherence Tracking: Logs medication usage history
    
    Refill Monitoring: Tracks remaining doses and alerts users

3	DESCRIPTION OF SYSTEM FUNCTIONS

3.1	Medication Management

3.2	Detailed Description of Function

This section provides a description of each function:
    
    •	Purpose: Allows users to manage medication records
    
    •	Initialization: User selects “Add Medication” or edits an existing entry
    
    •	Execution Options: Create, update, or delete medication entries
    
    •	Inputs: Medication name, dosage, frequency, schedule
    
    •	Outputs: Updated medication list and stored data
    
    •	Relationship: Links to reminders, adherence logs, and refill tracking
    
    •	Summary: Stores and maintains medication data for system use

3.3	Preparation of Function Inputs

Input Title: Medication Name
    
    •	Description: Name of prescribed medication
    
    •	Format: Text

Input Title: Dosage

    •	Description: Amount per intake
    
    •	Format: Numeric + unit (e.g., 10 mg)

Input Title: Frequency
    
    •	Description: Number of doses per day
    
    •	Format: Numeric

Input Title: Schedule Time
    
    •	Description: Time(s) for reminders
    
    •	Format: HH:MM

Example:
    
    •	Name: Lisinopril
    
    •	Dosage: 10 mg
    
    •	Frequency: 1 per day
    
    •	Time: 08:00 AM

3.4	Results
    
    •  Description: Medication is saved and displayed in list
    
    •  Output Form: Updated UI and stored database entry
    
    •  Report Generation: Used in adherence history and refill tracking
    
    •  Example Output: Medication appears in dashboard

4	OPERATING INSTRUCTIONS

4.1	Initiate Operation
    
    1.Install Remind Rx on Android Device
    
    2.Open the application
    
    3.Register or login
    
    4.Grant Notification permissions
    
    5.Acces and input data in main dashboard

4.2	Maintain Operation
    
    •  Regularly update medication entries
    
    •  Respond to reminder notifications
    
    •  Log doses after taking medication

4.3	Terminate and Restart Operations

To Exit:

    •	Close the application

To Restart:
    
    •	Close the application
    
    •	And log back in to the application.

5	ERROR HANDLING

    E001	Invalid Login	Incorrect credentials	Retry login
    E002	Missing Input	Required field empty	Complete all fields
    E003	Notification Disabled	Alerts cannot be sent	Enable notifications

5.1	HELP FACILITIES
    RemindRx includes:
        
    •	Basic in-app guidance through UI prompts
    
    •	Error messages for incorrect inputs

For additional help:

    •	Contact development team via email
    
    •	Future versions may include a dedicated help center

6	   IMPORTANT NOTES

   •	Privacy: RemindRx is a local-only prototype. All your data, including password hashes and medication history, is stored securely on your device and is not sent to any external server.
        
   •	Reminders: Ensure that the app has permission to send notifications and schedule exact alarms in your phone's system settings to receive timely reminders.



