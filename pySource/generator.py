# -*- coding: utf-8 -*-

from itertools import combinations

# 结点类
class Node:
    def __init__(self, ch = None, left = None, right = None, polar = 0, id = 0):
        self.ch = ch                                    # 变量或运算符
        self.left = left                                # 左孩子
        self.right = right                              # 右孩子
        self.polar = polar                              # 极性，可取 0, 1, -1
        self.id = id                                    # 结点编号

    def __str__(self):                                  # 把树转换成算式
        if self.ch not in '+-*/':
            return self.ch                              # 单变量不加括号
        left = str(self.left)                           # 左子树转字符串
        right = str(self.right)                         # 右子树转字符串
        if self.ch == '*' and self.left.ch == '-':
            left = '(' + left + ')'                     # 左子树加括号
        #if self.ch == '/' and self.right.ch in '+-*/' or self.ch in '*-' and self.right.ch in '+-':
        if self.ch == '*' and self.right.ch == '-':
            right = '(' + right + ')'                   # 右子树加括号
        return left + ' ' + self.ch + ' ' + right       # 用根结点的运算符相连



def naive5(left, right):
    yield Node('-', left, right)
    yield Node('-', right, left)
    if(left.ch != '*' and (right.ch!= '*') or left.id<right.left.id):
        yield Node('*', left, right)


# 枚举由 n 个变量组成的算式
def enum(n, actions, trees):
    def DFS(trees, minj):                                           # trees 为当前算式列表，minj 为第二棵子树的最小下标
        if len(trees) == 1:
            yield str(trees[0])                                     # 只剩一个算式，输出
            return
        for j in range(minj, len(trees)):                           # 枚举第二棵子树
            for i in range(j):                                      # 枚举第一棵子树
                for node in actions(trees[i], trees[j]):            # 枚举运算符
                    node.id = trees[-1].id + 1                      # 为新结点赋予 id
                    new_trees = [trees[k] for k in range(len(trees)) if k != i and k != j] + [node]
                                                                    # 从集合中去掉两棵子树，并加入运算结果
                    new_minj = 1
                                                                    # 若 actions 函数去重，则此处也避免「独立运算顺序不唯一」造成的重复
                    for expression in DFS(new_trees, new_minj):     # 递归下去
                        yield expression

    return DFS(trees, 1)


# 输入变量个数
import sys
#n = int(sys.argv[1])
n = 6
trees = [Node(chr(97 + i), id = i) for i in range(n)]           # 初始时有 n 个由单变量组成的算式

for nodeLen in range(1,n+1):
	for nodeCombination in combinations(trees,nodeLen):
		nodeComb_exps = set(enum(nodeLen,naive5,nodeCombination))
		for expression in nodeComb_exps:
			print(expression)
#naive5_exps = set(enum(n, naive5,trees))
#for expression in naive5_exps:
#    print(expression)
#print('Naive: %d expressions %d equivalent' % (len(naive_exps), len(naive5_exps)))
