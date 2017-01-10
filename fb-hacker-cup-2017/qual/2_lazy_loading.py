#!/usr/bin/python

import sys
import math

def get_max_trips(weights):
  weights = sorted(weights)
  trips = 0
  l = 0
  r = len(weights) - 1
  while l <= r:
    largest = weights[r]
    if largest >= 50:
      trips += 1
      r -= 1
      continue
    else:
      additional = math.ceil(50.0 / largest - 1)
      if (r - l) >= additional:
        l += additional
        r -= 1
        trips += 1
        continue
      else:
        break
  return trips

# Input
lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in range(cases):
  n = int(lines.next())
  weights = []
  for _ in range(n):
    weights.append(int(lines.next()))

  print 'Case #' + str(case+1) + ': ' + str(get_max_trips(weights))
