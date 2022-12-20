speakerRoles_d = {
    "Heber J. Grant*Conference Report":0,
    "George Albert Smith*Conference Report":0,
    "David O. McKay*Conference Report":0,
    "Joseph Fielding Smith*Conference Report":0
}
conferenceReports = [
    "Heber J. Grant*Conference Report",
    "George Albert Smith*Conference Report",
    "David O. McKay*Conference Report",
    "Joseph Fielding Smith*Conference Report"
]

with open("S:\\Users\\samwi\\Desktop\\Programming\\Fun\\Projects\\Citation-Index-Materialization\\Talk-Database-Optimization\\input.txt", "r") as infile:
    for line in infile:
        splitline = line.split(",")
        save = True
        for cr in conferenceReports:
            if cr in splitline[4]:
                speakerRoles_d[cr] += 1
                print(f"{cr}{speakerRoles_d[cr]}")
                save = False

        if splitline[4] in speakerRoles_d:
            speakerRoles_d[splitline[4]] += 1
        else:
            if save == True:
                speakerRoles_d[splitline[4]] = 1
                print(f"{splitline[4]}{speakerRoles_d[splitline[4]]}")
    print("\n")

for k, i in speakerRoles_d.items():
    print(f"{k} : {i}")