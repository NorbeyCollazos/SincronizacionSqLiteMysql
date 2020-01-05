<?php

class contactos
{

    // [contacto]
    const TABLA_PISCICOLA = 'piscicola';
    const ID_PISCICOLA = "idPiscicola";
    const NOMBRE_PISCICOLA = "NombrePiscicola";
    const ESPECIE = 'Especie';
    const PAIS = 'Pais';
    const DEPARTAMENTO = 'Departamento';
    const MUNICIPIO = 'Municipio';
    const VEREDA = 'Vereda';
    const AREA = 'Area';
    const CANTIDAD = 'Cantidad';
    const ID_USUARIO = 'idUsuario';
    const VERSION = 'version';
    // [/contacto]

    // [codigos]
    const ESTADO_EXITO = 100;
    const ESTADO_ERROR = 101;
    const ESTADO_ERROR_BD = 102;
    const ESTADO_MALA_SINTAXIS = 103;
    const ESTADO_NO_ENCONTRADO = 104;
    // [/codigos]

    // Campos JSON
    const INSERCIONES = "inserciones";
    const MODIFICACIONES = "modificaciones";
    const ELIMINACIONES = 'eliminaciones';


    public static function get($peticion)
    {
        $idUsuario = usuarios::autorizar();

        if (empty($peticion[0]))
            return self::obtenerContactos($idUsuario);
        else
            return self::obtenerContactos($idUsuario, $peticion[0]);

    }

    public static function post($segmentos)
    {
        $idUsuario = usuarios::autorizar();

        $payload = file_get_contents('php://input');

        $payload = json_decode($payload);

        $idContacto = contactos::insertar($idUsuario, $payload);

        http_response_code(201);
        return [
            "estado" => self::ESTADO_EXITO,
            "mensaje" => "Piscicola creado",
            "id" => $idContacto
        ];


    }

    public static function put($peticion)
    {
        $idUsuario = usuarios::autorizar();

        if (!empty($peticion[0])) {
            $body = file_get_contents('php://input');
            $contacto = json_decode($body);

            if (self::modificar($idUsuario, $contacto, $peticion[0]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::ESTADO_EXITO,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El contacto al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_MALA_SINTAXIS, "Falta id", 422);
        }
    }

    public static function delete($peticion)
    {
        $idUsuario = usuarios::autorizar();

        if (!empty($peticion[0])) {
            if (self::eliminar($idUsuario, $peticion[0]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::ESTADO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El contacto al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_MALA_SINTAXIS, "Falta id", 422);
        }

    }

    /**
     * Obtiene la colecci�n de contactos o un solo contacto indicado por el identificador
     * @param int $idUsuario identificador del usuario
     * @param null $idContacto identificador del contacto (Opcional)
     * @return array registros de la tabla contacto
     * @throws Exception
     */
    private function obtenerPiscicolas($idUsuario, $idPiscicola = NULL)
    {
        try {
            if (!$idPiscicola) {
                $comando = "SELECT * FROM " . self::TABLA_PISCICOLA .
                    " WHERE " . self::ID_PISCICOLA . "=?";

                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idUsuario
                $sentencia->bindParam(1, $idPiscicola, PDO::PARAM_INT);

            } else {
                $comando = "SELECT * FROM " . self::TABLA_PISCICOLA .
                    " WHERE " . self::ID_PISCICOLA . "=? AND " .
                    self::ID_USUARIO . "=?";

                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idContacto e idUsuario
                $sentencia->bindParam(1, $idPiscicola, PDO::PARAM_INT);
                $sentencia->bindParam(2, $idUsuario, PDO::PARAM_INT);
            }

            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

    /**
     * A�ade un nuevo contacto asociado a un usuario
     * @param int $idUsuario identificador del usuario
     * @param mixed $contacto datos del contacto
     * @return string identificador del contacto
     * @throws ExcepcionApi
     */
    private function insertar($idUsuario, $contacto)
    {
        if ($contacto) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

                // Sentencia INSERT
                $comando = 'INSERT INTO ' . self::TABLA_PISCICOLA . ' ( ' .
                    self::ID_PISCICOLA . ',' .
                    self::NOMBRE_PISCICOLA . ',' .
                    self::ESPECIE . ',' .
                    self::PAIS . ',' .
                    self::DEPARTAMENTO . ',' .
                    self::MUNICIPIO . ',' .
                    self::VEREDA . ',' .
                    self::AREA . ',' .
                    self::CANTIDAD . ',' .
                    self::ID_USUARIO . ')' .
                    ' VALUES(?,?,?,?,?,?,?,?,?,?)';

                // Preparar la sentencia
                $sentencia = $pdo->prepare($comando);

                // Generar Pk
                $idPiscicola = 'C-'.self::generarUuid();

                $sentencia->bindParam(1, $idPiscicola);
                $sentencia->bindParam(2, $NombrePiscicola);
                $sentencia->bindParam(3, $Especie);
                $sentencia->bindParam(4, $Pais);
                $sentencia->bindParam(5, $Departamento);
                $sentencia->bindParam(6, $Municipio);
                $sentencia->bindParam(7, $Vereda);
                $sentencia->bindParam(8, $Area);
                $sentencia->bindParam(9, $Cantidad);
                $sentencia->bindParam(10, $idUsuario);


                $NombrePiscicola = $contacto->NombrePiscicola;
                $Especie = $contacto->Especie;
                $Pais = $contacto->Pais;
                $Departamento = $contacto->Departamento;
                $Municipio = $contacto->Municipio;
                $Vereda = $contacto->Vereda;
                $Area = $contacto->Area;
                $Cantidad = $contacto->Cantidad;
               

                $sentencia->execute();

                // Retornar en el �ltimo id insertado
                return $idPiscicola;

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
        } else {
            throw new ExcepcionApi(
                self::ESTADO_MALA_SINTAXIS,
                utf8_encode("Error en existencia o sintaxis de par�metros"));
        }

    }

    /**
     * Actualiza el contacto especificado por idUsuario
     * @param int $idUsuario
     * @param object $contacto objeto con los valores nuevos del contacto
     * @param int $idContacto
     * @return PDOStatement
     * @throws Exception
     */
    private function modificar($idUsuario, $contacto, $idPiscicola)
    {
        try {
            // Creando consulta UPDATE
            $consulta = "UPDATE " . self::TABLA_PISCICOLA .
                " SET " . self::NOMBRE_PISCICOLA . "=?," .
                self::ESPECIE . "=?," .
                self::PAIS . "=?," .
                self::DEPARTAMENTO . "=? " .
                self::MUNICIPIO . "=? " .
                self::VEREDA . "=? " .
                self::AREA . "=? " .
                self::CANTIDAD . "=? " .
                " WHERE " . self::ID_PISCICOLA . "=? AND " . self::ID_USUARIO . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);

                $sentencia->bindParam(1, $idPiscicola);
                $sentencia->bindParam(2, $NombrePiscicola);
                $sentencia->bindParam(3, $Especie);
                $sentencia->bindParam(4, $Pais);
                $sentencia->bindParam(5, $Departamento);
                $sentencia->bindParam(6, $Municipio);
                $sentencia->bindParam(7, $Vereda);
                $sentencia->bindParam(8, $Area);
                $sentencia->bindParam(9, $Cantidad);
                $sentencia->bindParam(10, $idUsuario);


                $NombrePiscicola = $contacto->NombrePiscicola;
                $Especie = $contacto->Especie;
                $Pais = $contacto->Pais;
                $Departamento = $contacto->Departamento;
                $Municipio = $contacto->Municipio;
                $Vereda = $contacto->Vereda;
                $Area = $contacto->Area;
                $Cantidad = $contacto->Cantidad;

            // Ejecutar la sentencia
            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }


    /**
     * Elimina un contacto asociado a un usuario
     * @param int $idUsuario identificador del usuario
     * @param int $idContacto identificador del contacto
     * @return bool true si la eliminaci�n se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
    private function eliminar($idUsuario, $idPiscicola)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::TABLA_PISCICOLA .
                " WHERE " . self::ID_PISCICOLA . "=? AND " .
                self::ID_USUARIO . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idPiscicola);
            $sentencia->bindParam(2, $idUsuario);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

    /**
     * Inserta n elementos de seguidos en la tabla contactos
     * @param int $idUsuario identificador del usuario
     * @param mixed $contacto datos del contacto
     * @return string identificador del contacto
     * @throws ExcepcionApi
     */
    public static function insertarEnBatch(PDO $pdo, $listaContactos, $idUsuario)
    {
        // Sentencia INSERT
        $comando = 'INSERT INTO ' . self::TABLA_PISCICOLA . ' ( ' .
            self::ID_PISCICOLA . ',' .
            self::NOMBRE_PISCICOLA . ',' .
            self::ESPECIE . ',' .
            self::PAIS . ',' .
            self::DEPARTAMENTO . ',' .
            self::MUNICIPIO . ',' .
            self::VEREDA . ',' .
            self::AREA . ',' .
            self::CANTIDAD . ',' .
            self::ID_USUARIO . ',' .
            self::VERSION . ')' .
            ' VALUES(?,?,?,?,?,?,?,?,?,?,?)';

        // Preparar la sentencia
        $sentencia = $pdo->prepare($comando);

        $sentencia->bindParam(1, $idPiscicola);
        $sentencia->bindParam(2, $NombrePiscicola);
        $sentencia->bindParam(3, $Especie);
        $sentencia->bindParam(4, $Pais);
        $sentencia->bindParam(5, $Departamento);
        $sentencia->bindParam(6, $Municipio);
        $sentencia->bindParam(7, $Vereda);
        $sentencia->bindParam(8, $Area);
        $sentencia->bindParam(9, $Cantidad);
        $sentencia->bindParam(10, $idUsuario);
        $sentencia->bindParam(11, $version);

        foreach ($listaContactos as $item) {
            $idPiscicola = $item[self::ID_PISCICOLA];
            $NombrePiscicola = $item[self::NOMBRE_PISCICOLA];
            $Especie = $item[self::ESPECIE];
            $Pais = $item[self::PAIS];
            $Departamento = $item[self::DEPARTAMENTO];
            $Municipio = $item[self::MUNICIPIO];
            $Vereda = $item[self::VEREDA];
            $Area = $item[self::AREA];
            $Cantidad = $item[self::CANTIDAD];
            $version = $item[self::VERSION];
            $sentencia->execute();

        }

    }

    /**
     * Aplica n modificaciones de contactos
     * @param PDO $pdo instancia controlador de base de datos
     * @param $arrayContactos lista de contactos
     * @param $idUsuario identificador del usuario
     */
    public static function modificarEnBatch(PDO $pdo, $arrayContactos, $idUsuario)
    {

        // Preparar operaci�n de modificaci�n para cada contacto
        $comando = 'UPDATE ' . self::TABLA_PISCICOLA . ' SET ' .
            self::ID_PISCICOLA . ',' .
            self::NOMBRE_PISCICOLA . ',' .
            self::ESPECIE . ',' .
            self::PAIS . ',' .
            self::DEPARTAMENTO . ',' .
            self::MUNICIPIO . ',' .
            self::VEREDA . ',' .
            self::AREA . ',' .
            self::CANTIDAD . ',' .
            self::VERSION . '=? ' .
            ' WHERE ' . self::ID_PISCICOLA . '=? AND ' . self::ID_USUARIO . '=?';

        // Preparar la sentencia update
        $sentencia = $pdo->prepare($comando);

        // Ligar parametros
        $sentencia->bindParam(1, $NombrePiscicola);
        $sentencia->bindParam(2, $Especie);
        $sentencia->bindParam(3, $Pais);
        $sentencia->bindParam(4, $Departamento);
        $sentencia->bindParam(5, $Municipio);
        $sentencia->bindParam(6, $Vereda);
        $sentencia->bindParam(7, $Area);
        $sentencia->bindParam(8, $Cantidad);
        $sentencia->bindParam(9, $version);
        $sentencia->bindParam(10, $idPiscicola);
        $sentencia->bindParam(11, $idUsuario);

        // Procesar array de contactos
        foreach ($arrayContactos as $contacto) {
            $idPiscicola = $contacto[self::ID_PISCICOLA];
            $NombrePiscicola = $contacto[self::NOMBRE_PISCICOLA];
            $Especie = $contacto[self::ESPECIE];
            $Pais = $contacto[self::PAIS];
            $Departamento = $contacto[self::DEPARTAMENTO];
            $Municipio = $contacto[self::MUNICIPIO];
            $Vereda = $contacto[self::VEREDA];
            $Area = $contacto[self::AREA];
            $Cantidad = $contacto[self::CANTIDAD];
            $version = $contacto[self::VERSION];
            $sentencia->execute();
        }

    }

    /**
     * Aplina n elminaciones a la tabla 'contacto'
     * @param PDO $pdo instancia controlador de base de datos
     * @param $arrayIds lista de contactos
     * @param $idUsuario identificador del usuario
     */
    public static function eliminarEnBatch(PDO $pdo, $arrayIds, $idUsuario)
    {
        // Crear sentencia DELETE
        $comando = 'DELETE FROM ' . self::TABLA_PISCICOLA .
            ' WHERE ' . self::ID_PISCICOLA . ' = ? AND ' . self::ID_USUARIO . '=?';

        // Preparar sentencia en el contenedor
        $sentencia = $pdo->prepare($comando);


        // Procesar todas las ids
        foreach ($arrayIds as $id) {
            $sentencia->execute(array($id, $idUsuario));
        }

    }

    /**
     * Genera id aleatoria con formato UUID
     * @return string identificador
     */
    function generarUuid()
    {
        return sprintf('%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
            mt_rand(0, 0xffff), mt_rand(0, 0xffff),
            mt_rand(0, 0xffff),
            mt_rand(0, 0x0fff) | 0x4000,
            mt_rand(0, 0x3fff) | 0x8000,
            mt_rand(0, 0xffff), mt_rand(0, 0xffff), mt_rand(0, 0xffff)
        );
    }
}

