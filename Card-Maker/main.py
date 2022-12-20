import math


with open("complete-database.txt", "r") as database:
    # datapos = [-1, -1, -1, -1, -1]
    data = {}

    for line in database:
        splitline = line.split(",")
        vol = splitline[1]
        book = splitline[2]
        chap = splitline[3]
        ref = splitline[4]
        cit = splitline[4:9]


        if vol not in data:
            data[vol] = {}
        if book not in data[vol]:
            data[vol][book] = {}
        if chap not in data[vol][book]:
            data[vol][book][chap] = []
        data[vol][book][chap].append(cit)


# vol = "OT"
# book = "Gen."
batchsize = 10

batchedData = []

for vol in data:
    batchedVol = []

    for book in data[vol]:
        batchedBook = []

        for chap in data[vol][book]:
            batchedChap = []

            chapdat = data[vol][book][chap]
            chaplen = len(chapdat)
            numbatches = math.floor(chaplen/batchsize)
            
            batches = []
            for i in range(numbatches):
                batch = chapdat[i*10:(i*10)+10]
                batchedChap.append(batch)

            if (chaplen % batchsize) > 0:
                batch = chapdat[-(chaplen%batchsize):]
                batchedChap.append(batch)
            
            batchedBook.append(batchedChap)
        batchedVol.append(batchedBook)
    batchedData.append(batchedVol)


vol_i = 0
for vol in batchedData:
    print(f"[V-{vol_i}")
    book_i = 0
    for book in vol:
        print(f"\t[B-{book_i}")
        chap_i = 0
        for chap in book:
            print(f"\t\t[C-{chap_i}")
            batch_i = 0
            for batch in chap:
                print(f"\t\t\t[S-{batch_i}")
                for ref in batch:
                    print(f"\t\t\t\t{ref}")
                print("\t\t\t]")
                batch_i += 1
            print("\t\t]")
            chap_i += 1
        print("\t]")
        book_i += 1
    print("]")
    vol_i += 1