# prog2.2024.practico06

Escenario

Este ejercicio es atemporal, no maneja fechas ni horas. Tenemos una Clinica que atiende Paciente's de diferentes ObraSocial'es. El Paciente se comunica con la Clinica para solicitar un Turno con un Medico de una Especialidad en particular. El Paciente puede decidir si tomará el Turno con ObraSocial o en forma particular. Los Turno's son infinitos.
Cuando un Paciente inicia un Turno con un médico debe permanecer hasta terminarlo. Durante el Turno el Medico puede hacer una Receta o no. La Receta incluye uno a más Medicamento's.
Una vez que el Paciente finaliza el Turno está en condiciones de concurrir a la Farmacia si es que el Medico le entregó una Receta.
El Paciente debe iniciar y finalizar una Compra para comprar sus Medicamento's. La cantidad de Medicamento's en la Farmacia es infinito.


Anexo

La Farmacia tiene un Stock, en el caso que algún Medicamento no esté debe ser solicitado a la Drogueria. En este caso la Drogueria tiene stock infinito. Para esto debe hacerse el proceso iniciar y finalizar Pedido.


Comentarios de diseño

. Deben existir al menos 2 ObraSocial'es

. Deben existir al menos 4 Medico's por ObraSocial

. Algunos de esos Medico's atienden en forma particular

. Deben existir al menos 2 Especialidad'es

. Los Turno's existen hasta el final de la aplicación

. Las Receta's existen hasta el final de la aplicación

. Todas las "tablas" serán simuladas con Map's dentro de una clase DAO para gestionar las operaciones

. Definir la clase GestionTurnoService

. Definir la clase AtencionMedicoService

. Definir la clase GestionFarmaciaService

. La funcionalidad se demostrará a través de la batería de tests

. Las clases mencionadas representan las clases necesarias, no la totalidad, están en libertad de agregar las que sean necesarias.

. Se sugiere crear una clase que sirva de contenedor para aquellos objetos que no dependen directamente de otra clase.

. En general los objetos deberán instanciarse en el contenedor mencionado y ser inyectados donde sea necesario que sean manipulados.

. Las clases Service deberán implementar el patrón Singleton para asegurar una única existencia.


Algunas funcionalidades pedidas

. Se debe poder listar todos los médicos cuando se pide un turno particular

. Se debe poder listar los médicos de una especialidad indicando la obra social que atienden

. La cantidad y los medicamentos indicados en la Receta surgirán aleatoriamente siendo una opción no obtener una receta

