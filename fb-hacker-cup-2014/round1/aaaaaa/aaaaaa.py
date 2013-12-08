#!/usr/bin/python

import dijkstra_recipe
import sys
import toposort_recipe

prune_flag = True

def longest_queue(grid):
  #print grid
  graph = generate_graph(grid)

  print "original graph:"
  #print_graph(graph)
  print len(graph)
  return 1

  #print "pruned graph:"
  if prune_flag:
    graph = prune(graph, (0, 0, 'A', False))
  #print_graph(graph)

  print "done pruning"

  #print "reversing"
  reverse_graph = reverse_edges_set(graph)

  sorted_dag = toposort_recipe.toposort2(reverse_graph)
  weights = dict()
  for line in sorted_dag:
    for node in line:
      if reverse_graph[node]:
        weights[node] = max([weights[n] for n in reverse_graph[node]]) + 1
      else:
        weights[node] = 0

  #print '\n'.join(repr(x) for x in toposort_recipe.toposort2(reverse_graph))

  print "The weights are: "
  #print print_graph(weights)

#  (D, P) = dijkstra_recipe.Dijkstra(graph, (0, 0, 'A', False))
#  print "OK.... let's see"
#  print D
#  print "paths are:"
#  print P
  return max(weights.values()) + 1

def reverse_edges_set(graph):
  reverse = dict()
  for n in graph:
    for m in graph[n]:
      if m not in reverse:
        reverse[m] = set()
      reverse[m].add(n)

  return reverse

def print_graph(graph):
  nodes = sorted(graph.keys(), key=lambda x: 10*x[0]+x[1])
  for n in nodes:
    print str(n) + ' -> ' + str(graph[n])

def generate_graph(grid):
  # Node is (y, x, from('L', 'U', 'D', 'R'), used_up_or_down)
  nodes = list()
  x = len(grid)
  y = len(grid[0])
  for j in xrange(y):
    for i in xrange(x):
      if i is 0 and j is 0:
        nodes.append((0, 0, 'A', False))
      for d in ['L', 'R', 'D', 'U']:
        nodes.append((i, j, d, False))
        nodes.append((i, j, d, True))

  graph = dict()
  weight = -1

  for node in nodes:
    graph[node] = dict()

    # TODO(ipince): add cars

    # up neighbor
    x = node[0] - 1
    y = node[1]
    if x >= 0 and grid[x][y] is not '#' and node[2] is not 'U':
      if not node[3] or node[2] is 'D':
        # cannot go left if going up
        neighbor = (x, y, 'D', True)
        graph[node][neighbor] = weight

    # left neighbor
    x = node[0]
    y = node[1] - 1
    if y >= 0 and grid[x][y] is not '#' and node[2] is not 'L':
      if not node[3] or node[2] is 'R':
      # cannot go up if going left
        neighbor = (x, y, 'R', True)
        graph[node][neighbor] = weight

    # right neighbor
    x = node[0]
    y = node[1] + 1
    if y < len(grid[0]) and grid[x][y] is not '#' and node[2] is not 'R':
      neighbor = (x, y, 'L', node[3])
      graph[node][neighbor] = weight

    # down neighbor
    x = node[0] + 1
    y = node[1]
    if x < len(grid) and grid[x][y] is not '#' and node[2] is not 'D':
      neighbor = (x, y, 'U', node[3])
      graph[node][neighbor] = weight

  return graph

def prune(graph, origin):
  # remove crap not connected to origin (assumes not cyclic)
  connected = deep_neighbors(graph, origin)

  to_del = set()
  for node in graph:
    if node not in connected:
      to_del.add(node)

  for node in to_del:
    del graph[node]

  return graph

def deep_neighbors(graph, origin):
  nodes = set()
  nodes.add(origin)
  connected = set()
  while nodes:
    node = nodes.pop()
    nodes.update(graph[node])
    connected.add(node)
  return connected


#  neighbors = set()
#  neighbors.add(origin)
#  visit.update(graph[origin])
#  for n in graph[origin]:
#    neighbors.update(deep_neighbors(graph, n))
#  return neighbors

lines = iter(sys.stdin.readlines())
cases = int(lines.next())
for case in xrange(cases):
  [x, y] = lines.next().split()
  grid = []
  for i in xrange(int(x)):
    grid.append(list(lines.next().replace("\n", "")))
  print "Case #%d: %d" % (case + 1, longest_queue(grid))
