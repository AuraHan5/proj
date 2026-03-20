Per executar desde terminal les següents accions cal anar al directori FONTS fent "cd FONTS"   
make all -> Compilar todo para tener el ejecutable Main
make clean -> Borrar todos los archivos compilados
make test -> Hacer todos los test
make run -> Ejecutar Main
make execute_KMeans -> Ejecutar el KMeans driver para evaluar la precision del clustering

MANUAL D’USUARI
Al executar la comanda make run, apareixen a la interfície 4 opcions:
Gestionar enquesta
Aquesta funcionalitat permet fer la gestió de les enquestes de la app, des de crear-ne de noves fins a modificar-ne alguna existent, i inclús importar. Al premer el botó de “Gestionar enquesta” apareixen 4 noves funcionalitats:
Crea enquesta. L’usuari introdueix un títol i una descripció, i seguidament pot afegir preguntes del tipus que desitgi. A la part de sota de la interfície té dos símbols (+ i -) per afegir opcions a cada pregunta, a més dels botons corresponents per afegir/esborrar preguntes i per anar a la pregunta anterior/següent. Quan l’usuari és satisfet amb la enquesta creada, prem el botó de “Guardar i “Sortir” per desar la nova enquesta.
Importa enquesta. Quan l’usuari selecciona aquesta opció s’obre una pestanya que li permet buscar un fitxer (que conté una enquesta) en el seu ordinador. Al seleccionar el fitxer triat, la aplicació demana al usuari que introdueixi el nom i la descripció de la enquesta, i un cop fet això s’importa a la app i es torna disponible per respondre.
Modifica enquesta. Quan l’usuari vol modificar una enquesta existent, prem el botó, i a continuació li apareixen totes les enquestes existents per editar. Un cop selecciona l’enquesta qe vol, primer apareix l'opció de modificar títol i descripció. L’usuari, si ho desitja, fa els canvis que vol, i quan prem el botó “Guardar” és dirigit a les preguntes. L’usuari pot recorrer totes les preguntes de la enquesta i fer els canvis que desitgi, i finalment “Guardar i Sortir” quan estigui satisfet. Per a dur a terme aquesta funcionalitat cal tenir almenys una enquesta a la app, altrament sortirà un missatge d’error i es tornarà a la pàgina principal.
Esborra enquesta. Quan l’usuari prem el botó per esborrar una enquesta, li apareix un llistat de les enquestes, de les quals pot seleccionar-ne una. Un cop fet això, prem el botó per esborrar, i la aplicació, després de mostrar un missatge demanant confirmació al usuari, esborra la enquesta. Per a dur a terme aquesta funcionalitat cal tenir almenys una enquesta a la app, altrament sortirà un missatge d’error i es tornarà a la pàgina principal.

Respondre enquesta
Quan l’usuari selecciona “Respondre Enquesta”, és redirigit a una pantalla amb els noms de totes les enquestes. L’usuari en tria una, i un cop confirma la acció apareixeran per pantalla les preguntes, una a una. L’usuari les respon totes, movent-se amb les fletxes de “Anterior” i “Següent” per anar-les veient. Un cop finalitza, prem el botó de “Respondre” i es desen les respostes.

Analitzar enquesta de la App
Aquesta funcionalitat serveix per executar un anàlisi de clustering amb una enquesta de la App. Quan l’usuari tria aquesta opció, és redirigit a una pàgina que conté un llistat amb totes les enquestes disponibles. Un cop tria la que vol, clica el botó “Analitzar aquesta enquesta” i és redirigit a una altra pantalla. Aquí, l’usuari tria la k que desitja, i llavors prem el botó “Fer Anàlisi” que analitzarà la enquesta amb la k corresponent. Els resultats del clustering apareixen per pantalla. És important mencionar que la enquesta ha de tenir almenys 1 resposta per a poder ser analitzada, altrament la aplicació mostrarà un missatge d’error dient que la enquesta no té respostes.

Analitzar respostes importades
Aquesta funcionalitat serveix per fer un clustering a partir d’un dataset existent. Quan l’usuari tria aquesta opció, és conduit a una pestanya on apareix una opció per triar la k i un botó per analitzar respostes. Al prèmer aquest botó, s’obre el directori de l’usuari, on aquest pot seleccionar un dataset per a analitzar. Un cop el tria i confirma l’acció, es realitza l’anàlisis i surten els resultats per pantalla.
