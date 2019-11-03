import csv

def fmean(values):
    return sum(values)/len(values)

def make_average_csv(filenames, res_filename):
    result_writer = csv.writer(open(res_filename, mode='w'), delimiter=',', quoting=csv.QUOTE_NONE)
    readers = []
    for (index, filename) in enumerate(filenames): 
        readers.append(csv.reader(open(filename, mode='r'), delimiter=','))

    # print(readers)
    # print(zip(*readers))
    result_writer.writerow(["Generation", "Best fitness", "Maximum fitness", "Average fitness"])
    for rows in zip(*readers):
        # print(rows)
        # row row row row ... 

        mins = [min([float(val) for val in row[1::]]) for row in rows]
        maxs = [max([float(val) for val in row[1::]]) for row in rows]
        averages = [fmean([float(val) for val in row[1::]]) for row in rows]

        row_to_write = [int(rows[0][0]), fmean(
            mins), fmean(maxs), fmean(averages)]
        # print(row_to_write)
        result_writer.writerow(row_to_write)


experiments = [1, 2, 3, 4]
# experiments = [1]
tests = [1,2,3,4,5]
# crossovers = ["UNIFORM"]
# crossovers = ["ONE_POINT"]
crossovers = ["ONE_POINT", "UNIFORM"]
# mutations = ["SCRAMBLE"]
# mutations = ["INSERTION"]
# mutations = ["SCRAMBLE_INSERTION"]
mutations = ["SCRAMBLE", "INSERTION", "SCRAMBLE_INSERTION"]
mutation_rates = [0.0, 0.1]
# mutation_rates = [0.1]
# crossover_rates = [0.9]
crossover_rates = [0.9, 1.0]
original = ["true"]
# tournament_selection_types = ["BEST", "WEIGHTED"]
# tournament_selection_types = ["BEST"]
tournament_selection_types = ["WEIGHTED"]

filenames = []

for experiment in experiments:
    for test in tests:
        for crossover in crossovers:
            for mutation in mutations:
                for mutation_rate in mutation_rates:
                    for crossover_rate in crossover_rates:
                        for org in original:
                            for tournament in tournament_selection_types:
                                    filenames.append(
                                        "tests/fitnessex{0}_{1}_{2}_{3}_{4}_{5}_{6}_{7}.test.csv".format(experiment, test, crossover, mutation, mutation_rate, crossover_rate, org, tournament)
                                        )




print(filenames)
make_average_csv(filenames, "tweighted.csv")
    





    
