#!/bin/bash
set -e

docker exec postgres psql -U postgres -c 'select * from BATCH_JOB_EXECUTION;'