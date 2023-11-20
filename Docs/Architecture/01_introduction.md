This document is an introduction to the projects. It contains infos about the motivation, the drivers, the constraints,
and the goals of the project.

# Overview

tbd

# Quality goals

## Performance

The system is designed for a small crowd. There might be up to ten registered users that can act as GM and store files
for their campaigns. At any time, there may be not more than 50 users at the same time, and not more than 10 sessions
ongoing. Within this usage:
- for 90% of the requests the time between the request arriving at the system and the response being released to the
web shall be less than half a second, excluding the time for accessing the persistant data storage.
- for 99% of such requests, the same interval shall be less than two seconds, again excluding the time for accessing the
 persistant data storage.

## Accessibility

We seek for the WCAG 2.2 level AA recommendations by the W3C, as RPG fun is for everyone:
[WCAG 2.2 standard](https://www.w3.org/TR/WCAG22/)

# Stakeholder

| Stakeholders    | expectations                                                                              |
| :-------------- | :---------------------------------------------------------------------------------------- |
| Devs            | Easily understandable and expandable code                                                 |
| Service hosters | Secure and peformant web application                                                      |
| Service users   | Comprehensive web pages, as few clicks as possible, secure web application, accessibility |

