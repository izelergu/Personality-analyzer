import jpype
import sys

jpype.startJVM("C:\\Program Files\\Java\\jre1.8.0_202\\bin\\server\\jvm.dll", "-Djava.class.path=" + "C:\\Users\\izele\\PycharmProjects\\TwitterData\\zemberek-tum-2.0.jar", "-ea")
# Türkiye Türkçesine göre çözümlemek için gerekli sınıfı hazırla
TR = jpype.JClass("net.zemberek.tr.yapi.TurkiyeTurkcesi")
tr = TR()

Zemberek = jpype.JClass("net.zemberek.erisim.Zemberek")

zemberek = Zemberek(tr)

kelimeler = list()
f = open("src/main/samplefile1.txt", "r",  encoding='utf-8')
for line in f:
    kelimeler.append(line)

for kelime in kelimeler:
    if kelime.strip()>'':
        yanit = zemberek.asciiCozumle(kelime)
        if yanit:
            print("{}".format(yanit[0]))

        else:
            print("{} ÇÖZÜMLENEMEDİ".format(kelime))
#JVM kapat
jpype.shutdownJVM()
