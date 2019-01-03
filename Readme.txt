APD - TEMA 2
============

-> Initial vrajitorii pun pe coada de pe care ia minerul (tratam intai cazul unui singur miner), folosind putMessageWizardChannel(), cate un mesaj reprezentand parintele nodurilor root sau un nod root (o intrare in pestera), iar minerul preia fiecare dintre aceste mesaje, iar dupa fiecare 2 mesaje foloseste functia de hash, calculeaza noul string si pune mesajul cu noul string pe coada de pe care iau vrajitorii, folosind putMessageMinerChannel(). Evident, daca mesajul preluat de miner era (-1, END), minerul nu lua urmatorul mesaj din coada si nici nu folosea functia de hash;

-> Apoi, unul dintre vrajitori preia mesajul pus de unul dintre mineri, verifica faptul ca a fost aplicata corect functia de hash asupra string-ului din camera curenta si pune pe coada din care iau minerii 2*N+1 mesaje, unde fiecare 2 mesaje sunt nodul parinte, respectiv perechea (nod adiacent i, string nod adiacent i), N reprezinta numarul camerelor adiacente cu camera parinte si ultimul mesaj este perechea (-1,END). Aici intampinam urmatoarea problema: daca vrajitorii s-ar intersecta atunci cand pun mesaje pe coada, s-ar crea haos, astfel ca pe coada trebuie sa fie mesajele unui vrajitor puse consecutiv, nu trebuie sa avem mesajele vrajitorilor intercalate, asadar cand un vrajitor pune mesaje pe coada ceilalti trebuie sa astepte, fapt pe care il rezolvam folosind un reentrantlock si facand unlock() doar atunci cand ultimul mesaj pus pe coada este (-1,END);

-> Acum intra in discutie cazul in care avem mai multi mineri, caz in care intampinam urmatoarea problema: minerii se intercaleaza atunci cand iau mesaje de pe coada, astfel ca putem solutiona aceasta problema punand
intr-un synchronized cele 2 getMessageWizardChannel (preluarea celor 2 mesaje nod parinte, respectiv (nod adiacent i, string nod adiacent i) (in cazul in care primul mesaj nu este (-1,END))); 

-> Atunci cand unul dintre mineri pune pe coada de unde iau vrajitorii mesajul cu noul string hash-uit, acest miner adauga numarul camerei specifice mesajului in solved, pentru ca ulterior unul dintre mineri sa nu rezolve o camera rezolvata deja.
