def ligacaoTecnologias():
#     cursor.execute("select max(id_propriedade) from propriedade ")

#     for id in cursor:
#         ultimo_id = id
#         ultimo_id_propriedade = int(ultimo_id[0])

#     cursor.execute("select min(id_propriedade) from propriedade ")

#     for id in cursor:
#         ultimo_id = id
#         primeiro_id_propriedade = int(ultimo_id[0])

#     cursor.execute("select max(id) from tecnologias ")

#     for id in cursor:
#         ultimo_id = id
#         ultimo_id_tecnologia= int(ultimo_id[0])

#     cursor.execute("select min(id) from tecnologias ")

#     # for id in cursor:
#     #     ultimo_id = id
#     #     primeiro_id_tecnologia = int(ultimo_id[0])


#     # while primeiro_id_propriedade <= ultimo_id_propriedade:
#     #     ultimos_id = []

#     #     # for j in range(10):
#     #     #     flag = 1
#     #     #     sql = ("insert into tecnologia_has_propriedade (propriedade_id_propriedade, tecnologia_id_tecnologia, data_ligacao)"
#     #     #            "values (%s, %s, %s)")

#     #     #     while flag == 1:
#     #     #         flag = 0
#     #     #         id_tecnologia = random.randint(primeiro_id_tecnologia, ultimo_id_tecnologia)

#     #     #         for id in ultimos_id:
#     #     #             if id == id_tecnologia:
#     #     #                 flag = 1

#     #     #     ultimos_id.append(id_tecnologia)
#     #     #     params = [primeiro_id_propriedade, id_tecnologia, data_aleatoria()]
#     #     #     cursor.execute(sql, params)

#     #     primeiro_id_propriedade += 1
#     #     print("Relacionamento feito tecnologia")
#     #     db_connection.commit()
