@echo off
setlocal enabledelayedexpansion

del fontes.txt 2>nul

for /R %%f in (*.java,*.properties,*.yml,*.xml,Dockerfile) do (
    echo. >> fontes.txt
    echo ======================================== >> fontes.txt
    echo ARQUIVO: %%f >> fontes.txt
    echo ======================================== >> fontes.txt
    echo. >> fontes.txt
    type "%%f" >> fontes.txt
    echo. >> fontes.txt
)

echo Conteudo dos arquivos copiados para fontes.txt com nomes dos arquivos
pause