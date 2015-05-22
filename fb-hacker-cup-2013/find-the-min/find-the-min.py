#!/usr/bin/python

import sys


# insight 1: after t interations, we're left with a list of size k, whose numbers
# are all from 0 to k-1.

# insight 2: after such has been reached, remaining iterations are cyclic, with a
# period of k^2 - 1

def find_last2(k, n, a, b, c, r):
  # populate known numbers for m
  i = 0 # i always holds our iteration!
  m = [0] * k
  print len(m)
  m[0] = a
  append = m.append
  for i in range(1, k):
    #print "iter " + str(i) + ": " + repr(m)
    m[i] = (m[i - 1] * b + c) % r

  # create rolling window map of last k ints
  rolling = {}
  for i in m:
    try:
      rolling[i] += 1
    except KeyError:
      rolling[i] = 1

  # fill m up sequentially using rolling window; stop after 0 thru k-1 are in rolling
  #print "before first loop " + repr(m)
  for i in range(k, min(2*k + 1, n)): # insight.. 0-k condition must occur within k iterations
    #print "iter " + str(i) + ": " + repr(m)
    #if i % 1000 == 0:
    #  print "iter " + str(i)

    found = False
    prev_key = -1
    for key in sorted(rolling.keys()):
      if key - prev_key != 1:
        found = True
        break
      prev_key = key
    # after break, if found, prev_key+1 is smallest not in rolling
    if found:
      prev = m[i % k]
      m[i % k] = prev_key + 1
      rolling[prev_key + 1] = 1
      rolling[prev] -= 1
      if rolling[prev] == 0:
        del rolling[prev]

    if not found:
      i -= 1
      #print "BREAKING 2"
      break

    #for mini in range(k): # iterate from 0 to k-1 to find min not in rolling
    #  if mini not in rolling:
    #    # update m with new entry
    #    prev = m[i % k]
    #    m[i % k] = mini
    #    # add entry to rolling and remove previous entry, if any
    #    rolling[mini] = 1
    #    rolling[prev] -= 1
    #    if rolling[prev] == 0:
    #      del rolling[prev]
    #    break
    #if prev is None:
    #  i -= 1 # because last iteration didn't have any effect.
    #  print "BREAKING"
    #  break
  #print "Done with first loop!!"
  #print m
  # i has the index where we left off; or iteration # of the last one we DID
  remaining = n - (i+1) # num iterations to do - iterations already DONE
  if remaining == 0:
    #print "ENDING EARLY"
    return m[(n - 1) % k]

  #print "remaining " + str(remaining)
  num_lefts = remaining / (k + 1)
  skipped = num_lefts * (k + 1)

  i += skipped
  #print "Upped i by " + str(skipped) + ", so i is " + str(i)
  j = 0
  #for j in range(remaining - (num_lefts * (k + 1))):
  for j in range(i + 1, n):
    #print "iter " + str(j) + ": " + repr(m)
    #if j % 1000 == 0:
    #  print "iter " + str(j)

    found = False
    prev_key = -1
    for key in sorted(rolling.keys()):
      if key - prev_key != 1:
        found = True
        break
      prev_key = key


    smallest = k
    if found:
      smallest = prev_key + 1
    # after break, if found, prev_key+1 is smallest not in rolling
    # if not found, then k is smallest
    prev = m[j % k]
    m[j % k] = smallest
    rolling[smallest] = 1
    rolling[prev] -= 1
    if rolling[prev] == 0:
      del rolling[prev]

    #prev = None
    #for mini in range(k + 1): # iterate from 0 to k to find min not in rolling
    #  if mini not in rolling:
    #    # update m with new entry
    #    prev = m[j % k]
    #    m[j % k] = mini
    #    # add entry to rolling and remove previous entry, if any
    #    rolling[mini] = 1
    #    rolling[prev] -= 1
    #    if rolling[prev] == 0:
    #      del rolling[prev]
    #    break
 
  #print "final idx " + str(j) + ": " + repr(m)
  #return m[(i - num_lefts - 1) % k]
  return m[(n -1) % k]


def find_last(k, n, a, b, c, r):
  # populate known numbers for m
  m = [a]
  for i in range(1, k):
    print "iter " + str(i) + ": " + repr(m)
    m.append((m[i - 1] * b + c) % r)

  # create rolling window map of last k ints
  rolling = {}
  for i in m:
    if i in rolling:
      rolling[i] += 1
    else:
      rolling[i] = 1

  # fill m up sequentially using rolling window
  print "after first k: " + repr(m)
  i = 0
  for i in range(k, n):
    print "iter " + str(i) + ": " + repr(m)
    #print rolling
    prev = None
    for mini in range(k + 1): # iterate from 0 to k to find min not in rolling
      if mini not in rolling:
        prev = m[i % k]
        m[i % k] = mini
        rolling[mini] = 1
        break
    rolling[prev] -= 1
    if rolling[prev] == 0:
      del rolling[prev]

  print "iter final" + str(i) + ": " + repr(m)
  return m[n % k - 1]

case = 1
for line in sys.stdin.readlines():
  parts = line.split()
  if len(parts) == 1:
    num_cases = int(parts[0])
    continue

  if len(parts) == 2:
    k = int(parts[1])
    n = int(parts[0])
  elif len(parts) == 4:
    a = int(parts[0])
    b = int(parts[1])
    c = int(parts[2])
    r = int(parts[3])
    # assume k and n are parsed
    print 'Case #' + str(case) + ': ' + str(find_last2(k, n, a, b, c, r))
    case += 1

