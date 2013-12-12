#!/usr/bin/python

import random
import sys

def coins_game(coins, jars, target):
  min_moves = None
  for j in range(jars):
    unused = jars - (j + 1)
    moves = spread_evenly(coins, j+1, target) + unused
    if not min_moves:
      min_moves = moves
    min_moves = min(moves, min_moves)
  return min_moves

def spread_evenly(coins, jars, target):
  #print 'spreading %d coins in %d jars evenly, target: %d' % (coins, jars, target)
  rem = coins % jars
  empties = jars - rem
  assured = coins - rem

  if target <= assured:
    return target
  else:
    return target + empties

def generate_test_input():
  cases = 20
  print cases
  for _ in range(cases):
    jars = random.randint(1, 1000000)
    coins = random.randint(1, 1000000)
    target = random.randint(1, coins)
    print '%d %d %d' % (jars, coins, target)

#generate_test_input()
#exit()

lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in range(cases):
  [jars, coins, target] = lines.next().split()
  print "Case #%d: %d" % (case + 1, coins_game(int(coins), int(jars), int(target)))
