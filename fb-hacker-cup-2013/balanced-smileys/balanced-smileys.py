#!/usr/bin/python

import copy
import sys

def balanced_with_ints(text):
  stacks = set()
  stacks.add(0)

  #print text
  prev_colon = False
  for c in text:
    if c == '(':
      if prev_colon: # frowny face
        for stack in list(stacks): # copy is for parens
          stacks.add(stack + 1)
      else: # normal (
        stacks = set([stack + 1 for stack in stacks])
    elif c == ')':
      if prev_colon: # smiley face
        for stack in list(stacks): # copy is for parens
          if stack > 0:
            stacks.add(stack - 1)
      else:
        # normal ); decrease counts if count above 0
        stacks = set([stack - 1 for stack in stacks if stack > 0])
    prev_colon = c == ':'
    #print stacks

  # if any stack is at 0, we're good.
  if 0 in stacks:
    return 'YES'
  else:
    return 'NO'

def balanced(text):
  stacks = [[]]

  #print text
  prev_colon = False
  for c in text:
    if c == '(':
      if prev_colon: # frowny face
        for stack in copy.deepcopy(stacks): # copy is for parens
          stack.append('')
          stacks.append(stack)
      else: # normal (
        for stack in stacks:
          stack.append('')
    elif c == ')':
      if prev_colon: # smiley face
        for stack in copy.deepcopy(stacks): # copy is for parens
          if stack:
            stack.pop()
            stacks.append(stack)
      else:
        # splice out empty stacks
        stacks = [stack for stack in stacks if stack]
        for stack in stacks:
          stack.pop()
    prev_colon = c == ':'
    #print stacks

  # if any stack is empty, we're good.
  return len([stack for stack in stacks if not stack]) > 0

case = 0
for line in sys.stdin.readlines():
  if (case > 0):
    print 'Case #' + str(case) + ': ' + str(balanced_with_ints(line))
  case += 1
