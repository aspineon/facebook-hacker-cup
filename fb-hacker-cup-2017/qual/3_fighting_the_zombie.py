#!/usr/bin/python

import sys
import math
import itertools
import random
from collections import defaultdict

def precalculate_all():
  allprobs = defaultdict(lambda: defaultdict(int))
  for throws in xrange(1, 21):
    for faces in [4, 6, 8, 10, 12, 20]:
      print get_count(faces, throws, 3)
  return allprobs

def precalculate(faces, throws):
  sums = defaultdict(int)
  total = math.pow(faces, throws)
  for roll in itertools.product(range(1, faces+1), repeat=throws):
    sums[sum(roll)] += 1
  return { k: s / total for k, s in sums.iteritems() }

memo = defaultdict(int)

# matrix where row is the sum, column is number of throws
# m[x, y] = pr(sum is x, for y throws of dice)
# m[x, y+1] = m[x-i, y] for i in 1-6.
def get_count(faces, throws, s):
  #print 'calculating f,t,s (%d, %d, %d)' % (faces, throws, s)
  if memo[(faces, throws, s)]:
    return memo[(faces, throws, s)]
  if throws == 1:
    return 1 if s <= faces else 0
  # optimizations
  if s > faces * throws:
    return 0
  elif s < throws:
    return 0
  ans = 0
  for i in xrange(faces):
    if s - i - 1 > 0:
      ans += get_count(faces, throws - 1, s - i - 1)
  memo[(faces, throws, s)] = ans
  return ans

# opts is a list of tuples of (faces, throws, offset)
def optimal_prob(dice, damage):
  # calculate negative prob because it's faster (and builds the memo dict)
  probs = []
  for die in dice:
    under = 0
    #print 'die is ' + str(die) + ', damage is ' + str(damage)
    offset = die[2]
    for d in xrange(1, damage - offset):
      tmp = get_count(die[0], die[1], d)
      #print '  getting count for (faces, throws, damage) of (%d, %d, %d) -> %d' % (die[0], die[1], d, tmp)
      under += tmp
    total = float(math.pow(die[0], die[1]))
    probs.append((total - under) / total)
  #print probs
  return max(probs)

def generate_test_input():
  t = 1000
  print t
  for _ in xrange(t):
    h = random.randint(1, 10000)
    s = random.randint(2, 10)
    print '%d %d' % (h, s)
    dice = []
    for _ in xrange(s):
      x = random.randint(1, 20)
      y = [4, 6, 8, 10, 12, 20][random.randint(0, 5)]
      z = random.randint(0, 10000)
      dice.append(random.choice(['%dd%d+%d', '%dd%d-%d']) % (x, y, z))
    print ' '.join(dice)

#generate_test_input()
#sys.exit()

# Input
lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in range(cases):
  [damage, num] = lines.next().split()
  dice = []
  for d in lines.next().split():
    offset = 0
    if '+' in d:
      sp = d.split('+')
      offset = int(sp[1])
      d = sp[0]
    elif '-' in d:
      sp = d.split('-')
      offset = -int(sp[1])
      d = sp[0]
    d = d.split('d')
    dice.append((int(d[1]), int(d[0]), offset))

  print 'Case #' + str(case+1) + ': ' + ('%f' % (optimal_prob(dice, int(damage))))
