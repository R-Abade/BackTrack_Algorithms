import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("/home/abade/Documents/Programas/PAA/PAA_Algorithms/resultadosPD.txt", delim_whitespace=True)

print(df)

# Gráfico de comparações
plt.figure()
plt.plot(df["Tamanho"], df["Comparações"], marker="o")
plt.title("Comparações x Tamanho da Sequência")
plt.xlabel("Tamanho da sequência")
plt.ylabel("Número de comparações")
plt.grid(True)
plt.show()

# Gráfico de movimentos
plt.figure()
plt.plot(df["Tamanho"], df["Movimentos"], marker="o", color="orange")
plt.title("Movimentos x Tamanho da Sequência")
plt.xlabel("Tamanho da sequência")
plt.ylabel("Número de movimentos")
plt.grid(True)
plt.show()
