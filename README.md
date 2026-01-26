\# Multi-Tenant SaaS Platform (Microservices, AWS Free Tier)



\## Goal

Build a cloud-native SaaS backend supporting multi-tenancy, RBAC, async workflows, and file processing.



\## Architecture (MVP)

Client -> API Gateway -> Lambda (Spring Boot services) -> DynamoDB  

Async: Spring Boot -> SQS -> Node.js Worker -> DynamoDB  

Files: S3 stores files, DynamoDB stores metadata/status



\## Core Concepts

\- Multi-tenancy: tenantId is taken from JWT and used in all DB keys.

\- RBAC: roles in JWT (TENANT\_ADMIN, TENANT\_USER).

\- Async processing: /files/{id}/process enqueues a job in SQS.



\## Services

\- auth-service (Spring Boot): login + JWT

\- core-service (Spring Boot): tenants/users/files APIs

\- worker-service (Node.js): consumes SQS jobs, updates file status



\## API Endpoints (MVP)

Auth:

\- POST /auth/login

\- POST /auth/register (optional)



Tenant Admin:

\- POST /tenants/{tenantId}/users

\- GET  /tenants/{tenantId}/users



Files:

\- POST /files/presign

\- POST /files/{fileId}/process

\- GET  /files

\- GET  /files/{fileId}



\## DynamoDB Single Table Design

PK = TENANT#{tenantId}  

SK = USER#{userId} | FILE#{fileId}



Examples:

\- PK=TENANT#t1, SK=USER#u1

\- PK=TENANT#t1, SK=FILE#f1



File fields:

fileId, tenantId, uploaderUserId, s3Key, status, createdAt



