import argparse
import optparse

parser = argparse.ArgumentParser(description='test arg parser')
parser.add_argument('--number', type=float, help='a float number')
args=parser.parse_args()
print args.number

optparser = optparse.OptionParser()
optparser.add_option("--DIRTY", dest="dirty", help='boolean value')
(options, args) = optparser.parse_args()
print options.dirty
