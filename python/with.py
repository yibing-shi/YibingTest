with open("poem.txt") as f:
    while True:
        line = f.readline()
        print(line, end='')
