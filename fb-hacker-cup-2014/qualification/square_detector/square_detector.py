#!/usr/bin/python

import sys

def has_black_square_helper(grid):
  #for row in grid:
  #  print "Black line on " + str(row) + "? " + str(find_black_line(row))

  grid = iter(grid)

  # Skip leading all-white rows
  (idx, length) = find_black_line(grid.next())
  while (idx, length) == (0, 0):
    (idx, length) = find_black_line(grid.next())

  # Malformed first line => no square
  if idx == -1:
    return False

  # Make sure the following <length> lines are the same
  for i in range(length - 1):
    if find_black_line(grid.next()) != (idx, length):
      return False

  # Make sure the remaining lines, if any, are all white
  while True:
    try:
      if find_black_line(grid.next()) != (0, 0):
        return False
    except StopIteration:
      return True

  return False

def has_black_square(grid):
  try:
    return has_black_square_helper(grid)
  except StopIteration:
    return False

# Returns the index of the starting position and the length
# of a line of consecutive black squares. Or (-1, 0) is there
# is no such line but there is a black cell. Or (0, 0) if all
# cells are white.
def find_black_line(row):
  found_black = False
  completed_line = False
  index = 0
  line_length = 0
  for i in range(len(row)):
    if row[i] == '.' and found_black:
      completed_line = True
    if row[i] == '#':
      if completed_line:
        return (-1, 0)
      line_length += 1
      if not found_black:
        index = i
        found_black = True
  if found_black:
    return (index, line_length)
  else:
    return (0, 0)

# Input
lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in range(cases):
  n = int(lines.next())
  grid = []
  for _ in range(n):
    grid.append(list(lines.next())[:-1])

  print 'Case #' + str(case+1) + ': ' + ('YES' if has_black_square(grid) else 'NO')
