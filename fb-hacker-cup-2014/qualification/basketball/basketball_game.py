#!/usr/bin/python

# from http://code.activestate.com/recipes/577197-sortedcollection/ (modified)
from sorted_collection import SortedCollection
import random
import sys

debug = False
generate_input = False

def players_after_mins(players, active, mins):
  players = sorted(players,
      key=lambda p: p['shots'] * 1000 + p['height'], reverse=True)

  for i in range(len(players)):
    players[i]['draft'] = i
    players[i]['time'] = 0

  team1 = [players[i] for i in range(len(players)) if (i % 2) == 0]
  team2 = [players[i] for i in range(len(players)) if (i % 2) == 1]

  actives1 = team1[:active]
  bench1 = team1[active:]
  actives2 = team2[:active]
  bench2 = team2[active:]

  sort_key = lambda p: p['time'] * 1000 + p['draft']

  sorted_actives1 = SortedCollection(actives1, key=sort_key)
  sorted_bench1 = SortedCollection(bench1, key=sort_key)
  sorted_actives2 = SortedCollection(actives2, key=sort_key)
  sorted_bench2 = SortedCollection(bench2, key=sort_key)

  for m in range(mins):
    if debug:
      print
      print
      print "after %d mins" % m
      print "actives1: " + str(sorted_actives1)
      print "bench1: " + str(sorted_bench1)
      print
      print "actives2: " + str(sorted_actives2)
      print "bench2: " + str(sorted_bench2)

    update(sorted_actives1, sorted_bench1)
    update(sorted_actives2, sorted_bench2)

  active_players = [p['name'] for p in sorted_actives1] + \
                   [p['name'] for p in sorted_actives2]
  return ' '.join(sorted(active_players))

def update(actives, bench):
  if len(bench) == 0:
    return

  # keep bench sorted by time (low first), then by draft (low first)
  # keep actives sorted by time (high first), then by draft (high first)
  for p in actives: p['time'] += 1
  actives.refresh()

  exiting = actives[-1]
  entering = bench[0]

  actives.remove(exiting)
  actives.insert(entering)

  bench.remove(entering)
  bench.insert(exiting)

def generate_test_input():
  alphabet = list('abcdefghijklmnopqrstuvwxyz')
  print 50
  for i in range(50):
    p = random.randint(1, 5)
    n = random.randint(2 * p, 30)
    m = random.randint(1, 120)
    print '%d %d %d' % (n, m, p)
    for j in range(n):
      name = alphabet[random.randint(0, len(alphabet)-1)] + \
             alphabet[random.randint(0, len(alphabet)-1)]
      shots = random.randint(0, 100)
      height = random.randint(100, 240)
      print '%s %d %d' % (name, shots, height)

if generate_input:
  generate_test_input()
  exit()

lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in range(cases):
  (total_players, mins, active) = tuple(lines.next().split())
  players = []
  for i in range(int(total_players)):
    player = {}
    (name, shots, height) = tuple(lines.next().split())
    player['name'] = name
    player['shots'] = int(shots)
    player['height'] = int(height)
    players.append(player)
  print 'Case #' + str(case+1) + ': ' + players_after_mins(players, int(active), int(mins))

