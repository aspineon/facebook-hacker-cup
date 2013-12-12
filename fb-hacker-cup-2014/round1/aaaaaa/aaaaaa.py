#!/usr/bin/python

import copy
import random
import sys
import time

generate_test_input_flag = False

def longest_queue(grid):
  if grid[0][0] is '#':
    return 0

  if all_empty(grid):
    x = len(grid)
    y = len(grid[0])
    return x + y - 1 + 2*(max(x, y) -1)

  t0 = time.time()
  (graph, reverse_graph) = generate_graph(grid)
  t1 = time.time()
  #print "original graph of length %d took %d secs" % (len(graph), t1 - t0)
  #print_graph(graph)

  sorted_dag = topological_sort(
      dict(graph),
      copy.deepcopy(reverse_graph), (0, 0, 'A', False))
  t2 = time.time()
  #print "done sorting in %d secs" % (t2-t1)
  #print sorted_dag

  weights = dict()
  for node in sorted_dag:
    if reverse_graph[node]:
      weights[node] = max([weights[n] for n in reverse_graph[node]]) + 1
    else:
      weights[node] = 0
  t3 = time.time()

  #print "done finding weight in %d secs" % (t3-t2)
  #print print_graph(weights)

  return max(weights.values()) + 1

def all_empty(grid):
  for line in grid:
    for cell in line:
      if cell is not '.':
        return False
  return True

def topological_sort(graph, reverse_graph, origin):
  ordered = list()
  queue = set()
  queue.add(origin)
  while queue:
    node = queue.pop()
    ordered.append(node)
    for neighbor in graph[node]:
      if len(reverse_graph[neighbor]) > 1:
        # contains another edge, remove edge from node
        reverse_graph[neighbor].remove(node)
      else:
        del reverse_graph[neighbor]
        queue.add(neighbor)
    del graph[node]

  # TODO(ipince): graph has edges?
  return ordered

def print_graph(graph):
  nodes = sorted(graph.keys(), key=lambda x: 10*x[0]+x[1])
  for n in nodes:
    print str(n) + ' -> ' + str(graph[n])

def generate_graph(grid):
  # Node is (y, x, from('L', 'U', 'D', 'R'), used_up_or_down)
  graph = dict()
  reverse_graph = dict()

  nodes = set()
  nodes.add((0, 0, 'A', False))
  seen = set()
  while nodes:
    node = nodes.pop()
    if node not in graph:
      graph[node] = set()
    if node not in reverse_graph:
      reverse_graph[node] = set()

    seen.add(node)

    # up neighbor
    x = node[0] - 1
    y = node[1]
    if x >= 0 and grid[x][y] is not '#' and node[2] is not 'U':
      if not node[3] or node[2] is 'D':
        neighbor = (x, y, 'D', True)
        if neighbor not in seen: nodes.add(neighbor)
        graph[node].add(neighbor)
        if neighbor not in reverse_graph:
          reverse_graph[neighbor] = set()
        reverse_graph[neighbor].add(node)

    # left neighbor
    x = node[0]
    y = node[1] - 1
    if y >= 0 and grid[x][y] is not '#' and node[2] is not 'L':
      if not node[3] or node[2] is 'R':
        neighbor = (x, y, 'R', True)
        if neighbor not in seen: nodes.add(neighbor)
        graph[node].add(neighbor)
        if neighbor not in reverse_graph:
          reverse_graph[neighbor] = set()
        reverse_graph[neighbor].add(node)

    # right neighbor
    x = node[0]
    y = node[1] + 1
    if y < len(grid[0]) and grid[x][y] is not '#' and node[2] is not 'R':
      neighbor = (x, y, 'L', node[3])
      if neighbor not in seen: nodes.add(neighbor)
      graph[node].add(neighbor)
      if neighbor not in reverse_graph:
        reverse_graph[neighbor] = set()
      reverse_graph[neighbor].add(node)

    # down neighbor
    x = node[0] + 1
    y = node[1]
    if x < len(grid) and grid[x][y] is not '#' and node[2] is not 'D':
      neighbor = (x, y, 'U', node[3])
      if neighbor not in seen: nodes.add(neighbor)
      graph[node].add(neighbor)
      if neighbor not in reverse_graph:
        reverse_graph[neighbor] = set()
      reverse_graph[neighbor].add(node)

  return (graph, reverse_graph)

def generate_test_input():
  cases = 20
  print cases
  for c in xrange(cases):
    n = random.randint(1, 500)
    m = random.randint(1, 500)
    print '%d %d' % (n, m)
    for _ in xrange(n):
      line = ''
      for _ in xrange(m):
        line += random.choice(['.'] * 8 + ['#'] * 2)
      print line

if generate_test_input_flag:
  generate_test_input()
  exit()

lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in xrange(cases):
  [x, y] = lines.next().split()
  grid = []
  for i in xrange(int(x)):
    grid.append(list(lines.next().replace("\n", "")))
  print "Case #%d: %d" % (case + 1, longest_queue(grid))
