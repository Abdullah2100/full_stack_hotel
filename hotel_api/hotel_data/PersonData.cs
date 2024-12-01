using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hotel_data.dto;
using Npgsql;

namespace hotel_data
{
    public class PersonData
    {
        public static string connectionUrl = clsConnnectionUrl.url;

        /// <summary>
        /// Creates a new person entry in the database.
        /// </summary>
        /// <param name="personHolder">The PersonDto object containing person details.</param>
        /// <returns>True if the person was created successfully; otherwise, false.</returns>
        public static bool createPerson(
           PersonDto personHolder
            )
        {
            bool isCreated = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                INSERT INTO persons (name, phone, email, address)
                                VALUES (@name,@phone,@email,@address);
                                RETURNING personid;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {

                        cmd.Parameters.AddWithValue("@name", personHolder.name);
                        cmd.Parameters.AddWithValue("@phone", personHolder.phone);
                        cmd.Parameters.AddWithValue("@email", personHolder.email);
                        cmd.Parameters.AddWithValue("@address", personHolder.address);

                        var resultID = cmd.ExecuteScalar();
                        isCreated = ((long)resultID) > 0 ? true : false;
                    }
                }
                return isCreated;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person create {0} \n", ex.Message);

            }
            return isCreated;
        }

        /// <summary>
        /// Updates an existing person entry in the database.
        /// </summary>
        /// <param name="personHolder">The PersonDto object containing updated person details.</param>
        /// <returns>True if the person was updated successfully; otherwise, false.</returns>
        public static bool updatePerson(PersonDto personHolder)
        {
            bool isUpdate = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @"
                                UPDATE INTO persons 
                                SET name = @name, 
                                phone = @phone,
                                address=@address,
                                email=@email
                                WHERE personid =@id;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@id", personHolder.personID);
                        cmd.Parameters.AddWithValue("@name", personHolder.name);
                        cmd.Parameters.AddWithValue("@phone", personHolder.phone);
                        cmd.Parameters.AddWithValue("@email", personHolder.email);
                        cmd.Parameters.AddWithValue("@address", personHolder.address);

                        cmd.ExecuteNonQuery();
                        isUpdate = true;
                    }
                }
                return isUpdate;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person create {0} \n", ex.Message);

            }
            return isUpdate;
        }

        /// <summary>
        /// Deletes a person entry from the database.
        /// </summary>
        /// <param name="presonId">The ID of the person to delete.</param>
        /// <returns>True if the person was deleted successfully; otherwise, false.</returns>
        public static bool deletePerson(
          int presonId
       )
        {
            bool isDelelted = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" DELETE FROM  persons WHERE personid =@personID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@personID", presonId);

                        cmd.ExecuteNonQuery();
                        return isDelelted;
                    }
                }
                return isDelelted;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isDelelted;
        }

        /// <summary>
        /// Retrieves a person entry from the database by ID.
        /// </summary>
        /// <param name="presonId">The ID of the person to retrieve.</param>
        /// <param name="isDelelted">Reference parameter indicating if the person is deleted.</param>
        /// <returns>A PersonDto object if the person exists; otherwise, null.</returns>
        public static PersonDto? getPerson(
            long presonId, ref bool isDelelted
            )
        {
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE personid =@personID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@personID", presonId);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.HasRows)
                            {
                                isDelelted = (bool)result["isdeleted"];
                                var person = new PersonDto
                                (
                                    presonId,
                                    (string)result["name"],
                                    (string)result["phone"],
                                    (string)result["email"],
                                    result["address"] == DBNull.Value ? "" : (string)result["address"]
                                );
                            }
                        }
                    }
                }
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person getPerson by id {0} \n", ex.Message);

            }
            return null;
        }

        /// <summary>
        /// Retrieves a list of deleted persons from the database.
        /// </summary>
        /// <returns>A list of PersonDto objects representing deleted persons.</returns>
        public static List<PersonDto> getPersonsDeleted()
        {
            var personsList = new List<PersonDto>();
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE isdeleted = true";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {


                        using (var result = cmd.ExecuteReader())
                        {
                            while (result.Read())
                            {
                                var person = new PersonDto
                                (
                                    (long)result["personid"],
                                   (string)result["name"],
                                    (string)result["phone"],
                                    (string)result["email"],
                                    result["address"] == DBNull.Value ? "" : (string)result["address"]
                               );
                                personsList.Add(person);
                            }
                            return personsList;
                        }
                    }
                }
                return personsList;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person getPersons deleted by id {0} \n", ex.Message);

            }
            return personsList;
        }

        /// <summary>
        /// Retrieves a list of persons not marked as deleted from the database.
        /// </summary>
        /// <returns>A list of PersonDto objects representing persons not deleted.</returns>
        public static List<PersonDto> getPersonsNotDeleted()
        {
            var personsList = new List<PersonDto>();
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE isdeleted = false";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {


                        using (var result = cmd.ExecuteReader())
                        {
                            while (result.Read())
                            {
                                var person = new PersonDto
                              (
                                  (long)result["personid"],
                                  (string)result["name"],
                                    (string)result["phone"],
                                    (string)result["email"],
                                    result["address"] == DBNull.Value ? "" : (string)result["address"]
                                  );
                                personsList.Add(person);
                            }
                            return personsList;
                        }
                    }
                }
                return personsList;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person getPersons deleted by id {0} \n", ex.Message);

            }
            return personsList;
        }

        /// <summary>
        /// Checks if a person exists in the database by ID.
        /// </summary>
        /// <param name="presonId">The ID of the person to check for existence.</param>
        /// <returns>True if the person exists; otherwise, false.</returns>
        public static bool isExist(
            long presonId
            )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE personid =@personID;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@personID", presonId);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.Read())
                                isExist = true;
                        }
                    }
                }
                return isExist;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isExist;
        }

        /// <summary>
        /// Checks if a person exists in the database by name.
        /// </summary>
        /// <param name="name">The name of the person to check for existence.</param>
        /// <returns>True if the person exists; otherwise, false.</returns>
        public static bool isExist(
               string name
               )
        {
            bool isExist = false;
            try
            {
                using (var connection = new NpgsqlConnection(connectionUrl))
                {

                    connection.Open();

                    string query = @" SELECT * FROM  persons WHERE name =@name;";

                    using (var cmd = new NpgsqlCommand(query, connection))
                    {
                        cmd.Parameters.AddWithValue("@name", name);

                        using (var result = cmd.ExecuteReader())
                        {
                            if (result.Read())
                                isExist = true;
                        }
                    }
                }
                return isExist;
            }
            catch (Exception ex)
            {
                Console.WriteLine("\nthis error from person deleted {0} \n", ex.Message);

            }
            return isExist;
        }
    }
}