deret = [1, 3, 7, 2, 5, 6, 9, 8, 4]
n = len(deret)

for i in range(n):
    swapped = False
    for j in range(0, n - i - 1):
        if deret[j] > deret[j + 1]:
            deret[j], deret[j + 1] = deret[j + 1], deret[j]
            swapped = True
    if not swapped:
        break

print("Hasil sorting:", deret)