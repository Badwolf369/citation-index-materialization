
talksByEquinx = {}
with open("talk-index-input.txt", "r") as talkin:

    for line in talkin:
        splitline = line.split(",")
        if splitline[0] in talksByEquinx:
            talksByEquinx[splitline[0]].append(line)
        else:
            talksByEquinx[splitline[0]] = [line]

with open("complete-database.txt", "w") as outfile:
    outfile.write("")

with open("main-index-input.txt", "r") as mainin:
     for line in mainin:
        splitline = line.strip().split(",")

        if splitline[5] in talksByEquinx:
            for talk in talksByEquinx[splitline[5]]:
                splitalk = talk.strip().split(",")

                if splitline[6] == splitalk[3] and splitline[7] == splitalk[2]:
                    print(f"\ntalk:\n{line}matches talk:\n{talk}")

                    with open("complete-database.txt", "a") as outfile:
                        outfile.write(f"{splitline[0]},{splitline[1]},{splitline[2]},{splitline[3]},{splitline[4]},{splitalk[0]}-{splitalk[1]},{splitalk[2]},{splitalk[3]},{splitalk[4]},{splitalk[5]}\n")

                    break
            else:
                print(f"\ntalk:\n{line}does not match a talk in the database")
                break

