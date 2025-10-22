# Flask AWS Resource Monitor

## Overview
Flask application that displays AWS resources (EC2, VPCs, Load Balancers, AMIs).

## Requirements
- Python 3.11
- AWS credentials (environment variables)

## Docker Build
Multi-stage Dockerfile for optimized image size.

## Note
Application will show errors without AWS credentials - expected for assignment.
## Important: Loading Environment Variables

Before running the application, you must load AWS credentials as environment variables:

### Method 1: Export from .env file
```bash
export $(cat .env | xargs)
python app.py
