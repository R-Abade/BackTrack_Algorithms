import pandas as pd
import matplotlib.pyplot as plt

def loadFile(filepath):

    dados = []
    with open(filepath, "r", encoding="utf-8") as f:
        linhas = f.readlines()[2:]  # pula cabeçalho
        for linha in linhas:
            partes = linha.split()
            if len(partes) >= 3:
                n = int(partes[0])
                mov = partes[1]
                comp = partes[2]

                if mov != "-" and comp != "-":
                    dados.append({
                        "n": n,
                        "movimentos": int(mov),
                        "comparacoes": int(comp),
                    })
    return pd.DataFrame(dados)

# Carregar dados
df_warnsdorff = loadFile("/home/abade/Documents/Programas/PAA/PAA_Algorithms/warnsdorff_variando_n.txt")
df_sem = loadFile("/home/abade/Documents/Programas/PAA/PAA_Algorithms/semWarnsdorff_variando_n.txt")

# comparações
plt.figure(figsize=(8,6))
plt.plot(df_warnsdorff["n"], df_warnsdorff["comparacoes"], marker="o", label="Com Warnsdorff")
plt.plot(df_sem["n"], df_sem["comparacoes"], marker="s", label="Sem Warnsdorff")
plt.xlabel("Tamanho do tabuleiro (n)")
plt.ylabel("Número de comparações (escala log)")
plt.yscale("log")  # Escala logarítmica
plt.title("Comparações x Tamanho do tabuleiro")
plt.legend()
plt.grid(True, which="both", linestyle="--", linewidth=0.7)
plt.tight_layout()
plt.savefig("/home/abade/Documents/Programas/PAA/PAA_Algorithms/comparacoes.png", dpi=300)
plt.show()

# movimentos
plt.figure(figsize=(8,6))
plt.plot(df_warnsdorff["n"], df_warnsdorff["movimentos"], marker="o", label="Com Warnsdorff")
plt.plot(df_sem["n"], df_sem["movimentos"], marker="s", label="Sem Warnsdorff")
plt.xlabel("Tamanho do tabuleiro (n)")
plt.ylabel("Número de movimentos (escala log)")
plt.yscale("log")  # Escala logarítmica
plt.title("Movimentos x Tamanho do tabuleiro")
plt.legend()
plt.grid(True, which="both", linestyle="--", linewidth=0.7)
plt.tight_layout()
plt.savefig("/home/abade/Documents/Programas/PAA/PAA_Algorithms/movimentos.png", dpi=300)
plt.show()
