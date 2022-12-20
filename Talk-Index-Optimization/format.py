sessionRenameKey = {
    "Thursday Morning":     "RM",
    "Thursday Afternoon":   "RA",
    "Friday Morning":       "FM",
    "Friday Afternoon":     "FA",
    "Saturday Morning":     "SM",
    "Saturday Afternoon":   "SA",
    "Saturday Evening":     "SE",
    "Sunday Morning":       "NM",
    "Sunday Afternoon":     "NA",
    "Monday Morning":       "MM",
    "Monday Afternoon":     "MA",
    "Monday Evening":       "ME",
    "Tuesday Morning":      "TM",
    "Tuesday Afternoon":    "TA",
    "Wednesday Morning":    "WM",
    "Wednesday Afternoon":  "WA",

    "Priesthood":           "PH",
    "Priesthood Session":   "PH",
    "Women's Fireside":     "WM",
    "Women's Meeting":      "WM",
    "Women's Session":      "WM",

    "Church of the Air":    "CA",
    "Young Women":          "YW",
    "Relief Society":       "RS",
    "RS Sesquicentennial":  "RS",
    "Welfare":              "WF",

    "Solemn Assembly":      "Us",
    "Funeral Services":     "Up",
    "Special Broadcast":    "UU",
    "Special Message":      "UU",
    "Peru Sermon":          "UU",
    "RR Seminar":           "UU",
    "Postal Card":          "UU",
    "Priesthood Leadership":"UU",
    "Fireside":             "UU",
    "World Religions":      "UU",
    "Satellite Broadcast":  "UU"
}


with open("output.txt", "w") as outfile:
    outfile.write("")

with open("input.txt", "r") as infile:

    line_i = 0
    for line in infile:
        line = line.strip("\n;")
        line = line.split(",")

        sessionCode = line[1]
        sessionCode = sessionCode.split("-")
        sessionCode[0] = sessionRenameKey[sessionCode[0]]
        line[1] = f"{sessionCode[0]}-{int(sessionCode[1])+1}"

        with open("output.txt", "a") as outfile:
            outfile.write(f"{line[0]},{line[1]},{line[2]},{line[3]},{line[4]}\n")
            print(line_i)
        line_i += 1


