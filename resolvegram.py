# Convert txt ngram files to json.
import json

folder = "assets/"
files = ["english_words", "english_bigrams", "english_trigrams", "english_quadgrams", "english_quintgrams"]
ext = "txt"
MAX_VALS = 700

for filename in files:
    f = open(folder + filename + "." + ext, "r")
    lines = f.readlines()

    total_occr = 0

    read_array = []

    for line in lines:
        arr = line.split(" ")
        name = arr[0]
        value = int(arr[1])
        # print(name, value)
        read_array.append((name, value))
        total_occr = total_occr + value

    w = open(folder + filename + "_frequency.json", "w+")

    result_dict = {}

    count = 0
    for pair in read_array:
        # print("wut")
        if count > MAX_VALS:
            break
        result_dict[pair[0].lower()] = pair[1] / total_occr
        count = count + 1

    # print(result_dict)

    w.write(json.dumps(result_dict))
