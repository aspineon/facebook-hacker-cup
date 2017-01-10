#!/usr/bin/python

import sys
import math

def is_black(pct, x, y):
  #print "point is (%d, %d) . pct is %d" % (x, y, pct)
  x -= 50
  y -= 50
  if pct == 0:
    return False
  if x == 0 and y == 0:
    return pct > 0
  pct = 2 * math.pi * pct / 100
  d = math.sqrt(x*x + y*y)
  alpha = math.atan2(y, x)
  if alpha < 0:
    alpha = 2 * math.pi + alpha
  #print 'radial coords are (%s, %s, %s deg)' % (str(d), str(alpha), str(alpha * 180/math.pi))
  top = math.pi / 2 - alpha
  if top < 0:
    top = 2 * math.pi + top
  #print 'pct is %s. converted angle is %s (%s deg)' % (str(pct), str(top), str(top * 180 / math.pi))

  return d < 50 and top < pct

# Input
lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in range(cases):
  [pct, x, y] = lines.next().split()
  black = is_black(int(pct), float(x), float(y))

  print 'Case #' + str(case+1) + ': ' + ('black' if black else 'white')
