#!/bin/bash

export BASE_URL=https://producer.shukawam.me
k6 run load-generator.js
