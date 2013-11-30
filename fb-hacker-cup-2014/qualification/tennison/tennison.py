#!/usr/bin/python

from decimal import *

import sys

getcontext().prec = 6

def match_probability(
  sets, p_win_sunny, p_win_rain, p_next_sunny,
  sun_increase, p_sun_increase,
  sun_decrease, p_sun_decrease):

  sets = int(sets)
  p_win_sunny = Decimal(p_win_sunny)
  p_win_rain = Decimal(p_win_rain)
  p_next_sunny = Decimal(p_next_sunny)
  sun_increase = Decimal(sun_increase)
  p_sun_increase = Decimal(p_sun_increase)
  sun_decrease = Decimal(sun_decrease)
  p_sun_decrease = Decimal(p_sun_decrease)

  print sets
  print p_win_sunny
  print p_win_rain
  print p_next_sunny
  print sun_increase
  print sun_decrease
  print p_sun_increase
  print p_sun_decrease

  return match_prob_helper(
      sets, 0, 0,
      p_win_sunny, p_win_rain, p_next_sunny,
      sun_increase, p_sun_increase,
      sun_decrease, p_sun_decrease)

cache = {}

# Return prob of winning match of <sets> sets, given that Tennison has already
# won <won> of them.
def match_prob_helper(sets, won, lost,
    p_win_sunny, p_win_rain, p_next_sunny,
    sun_increase, p_sun_increase,
    sun_decrease, p_sun_decrease):

  k = ','.join(map(str, [sets, won, lost, p_next_sunny]))
  if k in cache:
    return cache[k]

  if sets == won:
    return Decimal('1')
  elif sets == lost:
    return Decimal('0')
  p_win_current = p_next_sunny * p_win_sunny + (1 - p_next_sunny) * p_win_rain
  #print "Sets: %s; won: %s; lost: %s; p_win_current: %s; p_next_sunny: %s" % (sets, won, lost, p_win_current, p_next_sunny)

  # scenario 1: tennison wins this set. => win match if he wins the rest.
  p_sunny_win = p_next_sunny + sun_increase*p_sun_increase
  if p_sunny_win > Decimal('1') : p_sunny_win = Decimal('1')

  p_remainder_if_win = match_prob_helper(
      sets, won+1, lost,
      p_win_sunny, p_win_rain, p_sunny_win,
      sun_increase, p_sun_increase,
      sun_decrease, p_sun_decrease)

  # scenario 2: tennison loses this set. can he still win? only if he
  # wins the remainder and hasn't lost already.
  p_sunny_lose = p_next_sunny - sun_decrease*p_sun_decrease
  if p_sunny_lose < Decimal('0') : p_sunny_lose = Decimal('0')

  p_remainder_if_lose = match_prob_helper(
      sets, won, lost+1,
      p_win_sunny, p_win_rain, p_sunny_lose,
      sun_increase, p_sun_increase,
      sun_decrease, p_sun_decrease)

  p_win_overall = p_win_current * p_remainder_if_win + \
            (1 - p_win_current) * p_remainder_if_lose
  cache[k] = p_win_overall
  return p_win_overall

#  for s in range(2 * sets - 1):
#    print "p_next_sunny: " + str(p_next_sunny)
#    p_win_current = p_next_sunny * p_win_sunny + (1 - p_next_sunny) * p_win_rain
#    p_wins.append(p_win_current)
#    p_next_sunny = p_win_current * (p_next_sunny + (sun_increase * p_sun_increase)) \
#           + (1 - p_win_current) * (p_next_sunny - (sun_decrease * p_sun_decrease))
#    if p_next_sunny < Decimal(0): p_next_sunny = Decimal(0)
#    if p_next_sunny > Decimal(1): p_next_sunny = Decimal(1)
#
#  return p_wins

case = 0
for line in sys.stdin.readlines():
  if case != 0:
    inputs = tuple(line.split())
    print 'Case #' + str(case) + ': ' + str(match_probability(*inputs))
  case += 1
