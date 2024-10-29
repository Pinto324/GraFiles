db.createCollection("usuario");
db.createCollection("archivo");
db.usuario.insertMany([
    {
        Username: "admin",
        Password: "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b",
        Rol: "Admin"
    },
    {
        Username: "empleado",
        Password: "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b",
        Rol: "Empleado"
    }
]);
db.archivo.insertMany([
    {
        _id: ObjectId('507f1f77bcf86cd799439011'),
        Nombre: "Inicio",
        Extension: "carpeta",
        PadreId: ObjectId('726f6f740000000000000000'),
        FechaCreacion: "2024-10-28",
        PropietarioId: "671fbeabbb69b581adfe6912",
        Habilitado: true
    },
    {
        Nombre: "Foto",
        Extension: "png",
        PadreId:ObjectId('726f6f740000000000000000'),
        FechaCreacion: "2024-10-27",
        FechaModificacion: "2024-10-27",
        Contenido:"../../../Archivos/Prueba.png",
        PropietarioId: "671fbeabbb69b581adfe6912",
        Habilitado: true
    },
    {
        Nombre: "Sistema",
        Extension: "carpeta",
        PadreId: ObjectId('507f1f77bcf86cd799439011'),
        FechaCreacion: "2024-10-28",
        PropietarioId: ObjectId('671fbeabbb69b581adfe6912'),
        Habilitado: true
    }  
]);
//Genera un documento para mongoDB de la coleccion archivos  null ""
