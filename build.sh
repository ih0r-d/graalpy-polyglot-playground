#!/bin/sh

rm -r python-resources/ || true && ./mvnw clean package
