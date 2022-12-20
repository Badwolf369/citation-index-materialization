import re



volumeRenameKey = {
    "Old Testament": "OT",
    "New Testament": "NT",
    "Book of Mormon": "BOM",
    "Doctrine and Covenants": "DC",
    "Pearl of Great Price": "PGP"}

bookRenameKey = {

}

print("")

with open("output.csv", "w") as outfile:
    outfile.write("")

with open("InputDB.txt") as infile:

    line_i = 0
    newLine = ""

    for line in infile:

        splitLine  = line.split()

        if line[0] == "V":

            # volumeIndex = f"V{splitLine[0][2]}"

            volumeName = ""
            for i in splitLine[1:-1]:
                if i == splitLine[-2]:
                    volumeName += f"{i}"
                else:
                    volumeName += f"{i} "
            volumeName = volumeRenameKey[volumeName]

            # volumeCitationCount = splitLine[-1].strip("[]")
            
            # volumeLine = f"{volumeIndex},{volumeName},{volumeCitationCount}"
            # with open("output.csv", "a") as outfile:
            #     print(volumeLine)
            #     outfile.write(f"{volumeLine}\n")

        elif line[0:2] == " B":

            # bookIndex = f"B{splitLine[0][2:4]}"
            
            bookName = "\""
            for i in splitLine[1:-1]:
                if i == splitLine[-2]:
                    bookName += f"{i}\""
                else:
                    bookName += f"{i} "

            # bookCitationCount = splitLine[-1].strip("[]")

            # bookLine = f",,,{bookIndex},{bookName},{bookCitationCount}"
            # with open("output.csv", "a") as outfile:
            #     print(bookLine)
            #     outfile.write(f"{bookLine}\n")

        elif line[0:3] == "  C":

            # splitLine[0] = splitLine[0].strip("C-.")
            # chapterIndex = f"C{splitLine[0].zfill(3)}"

            chapter = f"chapter {splitLine[2]}"

            # chapterCitationCount = splitLine[-1].strip("[]")

            # chapterLine = f",,,,,,{chapterIndex},{chapter},{chapterCitationCount}"
            # with open("output.csv", "a") as outfile:
            #     print(chapterLine)
            #     outfile.write(f"{chapterLine}\n")

        
        elif line[0:4] == "   R":

            # splitLine[0] = splitLine[0].strip("R-.")
            # referenceIndex = f"C{splitLine[0].zfill(3)}"

            reference = ""
            for i in splitLine[1:]:
                if i == splitLine[-1]:
                    reference += f"{i}"
                else:
                    reference += f"{i} "

            f = reference.find("[")
            if f != -1:
                reference = reference[:f]
            reference.strip(".")
            splitReference = re.split(" |:", reference)
            if len(splitReference) == 2:
                referenceData = f"{volumeName},{splitReference[0]},{splitReference[1]},1,"

            elif len(splitReference) == 3:
                splitReference[2] = splitReference[2].replace(",", ";")

                referenceData = f"{volumeName},{splitReference[0]},{splitReference[1]},{splitReference[2]}"

            elif len(splitReference) > 3:
                bookname = ""
                for i in splitReference[:-3]:
                    if i == "of":
                        i = "o"
                    bookname += f"{i}"
                bookname += f"{splitReference[-3]}"

                splitReference[-1] = splitReference[-1].replace(",", ";")

                referenceData = f"{volumeName},{bookname},{splitReference[-2]},{splitReference[-1]}"

        elif line[0:5] == "    T":
            year = line[6:10]
            month = line[11]

            splitLine = line.split("\"")
            splitLine[0] = splitLine[0].strip()
            f = splitLine[0].find(", ")
            splitLine[0] = splitLine[0][f+2:]

            speaker = f"\"{splitLine[0]}\""
            title = f"\"{splitLine[1]}\""

            newLine = f"{line_i},{referenceData},{year}-{month},{speaker},{title}"

            print(newLine)
            with open("output.csv", "a") as outfile:
                outfile.write(f"{newLine}\n")

            line_i += 1




print("")