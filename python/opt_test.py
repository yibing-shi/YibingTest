import optparse

parser = optparse.OptionParser()
parser.add_option("--number", dest="number", type=float, help="a float", metavar="FILE")
(options, args) = parser.parse_args()

print(options.number)

