#!/usr/bin/python

from collections import defaultdict
import operator
import sys

allowed_chars = 'abcdefghijklmnopkrstuvwxyz'

def hist(text):
  counts = defaultdict(lambda: 0)
  for c in text:
    counts[c] += 1
  return counts

def max_beauty(text):
  counts = hist(text.lower())

  max_beauty = 0
  next_score = 26
  for tup in sorted(counts.iteritems(), key=operator.itemgetter(1), reverse=True):
    if tup[0] in allowed_chars:
      max_beauty += next_score * tup[1]
      next_score -= 1
  return max_beauty

case = 0
for line in sys.stdin.readlines():
  if (case > 0):
    print 'Case #' + str(case) + ': ' + str(max_beauty(line))
  case += 1
