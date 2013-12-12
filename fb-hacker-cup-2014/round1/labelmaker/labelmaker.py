#!/usr/bin/python

import sys

def last_box_label(alphabet, boxes):
  n = len(alphabet)
  if n == 1:
    return "".join(alphabet for _ in range(boxes))
  label = []
  carry = False
  while boxes > 0:
    rem = boxes % n
    if carry: rem = (rem - 1) % n
    boxes = boxes // n
    if rem is 0 and boxes is 0:
      break
    label.append(alphabet[rem-1])
#    print 'boxes: %d ; rem: %d ; adding: %s' % (boxes, rem, alphabet[rem-1])
    if rem == 0:
      carry = True
    else:
      carry = False

  return "".join(reversed(label))


lines = iter(sys.stdin.readlines())

cases = int(lines.next())
for case in range(cases):
  [alphabet, boxes] = lines.next().split()
  print "Case #%d: %s" % (case + 1, last_box_label(alphabet, int(boxes)))

