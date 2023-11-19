package com.novianto.challange6.util.storeprocedure;

import org.springframework.stereotype.Component;

@Component
public class StoreProcedureUser {

    public String getAllUser = "CREATE OR REPLACE FUNCTION public.get_all_users()\n" +
            "RETURNS TABLE (\n" +
            "    id UUID,\n" +
            "    username CHARACTER VARYING,\n" +
            "    email_address CHARACTER VARYING,\n" +
            "    password CHARACTER VARYING, -- Gunakan VARCHAR untuk password yang belum di-hash\n" +
            "    created_date TIMESTAMPTZ,\n" +
            "    updated_date TIMESTAMPTZ,\n" +
            "    deleted_date TIMESTAMPTZ\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        u.id,\n" +
            "        u.username,\n" +
            "        u.email_address,\n" +
            "        u.password,\n" +
            "        u.created_date,\n" +
            "        u.updated_date,\n" +
            "        u.deleted_date\n" +
            "    FROM\n" +
            "        \"user\" u;\n" +
            "END;\n" +
            "$function$;\n";

    public String getUserById = "CREATE OR REPLACE FUNCTION public.get_user_by_id(p_user_id UUID)\n" +
            "RETURNS TABLE (\n" +
            "    id UUID,\n" +
            "    username CHARACTER VARYING,\n" +
            "    email_address CHARACTER VARYING,\n" +
            "    password CHARACTER VARYING,\n" +
            "    created_date TIMESTAMPTZ,\n" +
            "    updated_date TIMESTAMPTZ,\n" +
            "    deleted_date TIMESTAMPTZ\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        u.id,\n" +
            "        u.username,\n" +
            "        u.email_address,\n" +
            "        u.password,\n" +
            "        u.created_date,\n" +
            "        u.updated_date,\n" +
            "        u.deleted_date\n" +
            "    FROM\n" +
            "        \"user\" u\n" +
            "    WHERE\n" +
            "        u.id = p_user_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String insertUser = "CREATE OR REPLACE FUNCTION public.insert_user(\n" +
            "    p_username CHARACTER VARYING,\n" +
            "    p_email_address CHARACTER VARYING,\n" +
            "    p_password CHARACTER VARYING\n" +
            ")\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "DECLARE\n" +
            "    hashed_password CHARACTER VARYING;\n" +
            "BEGIN\n" +
            "    hashed_password := crypt(p_password, gen_salt('bf', 8));\n" +
            "    \n" +
            "    INSERT INTO \"user\" (username, email_address, password, created_date, updated_date, deleted_date)\n" +
            "    VALUES (p_username, p_email_address, hashed_password, NOW(), NOW(), NULL);\n" +
            "END;\n" +
            "$function$;\n";

    public String updateUser = "CREATE OR REPLACE FUNCTION public.update_user(\n" +
            "    p_user_id UUID,\n" +
            "    p_username CHARACTER VARYING,\n" +
            "    p_email_address CHARACTER VARYING,\n" +
            "    p_password CHARACTER VARYING\n" +
            ")\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE \"user\"\n" +
            "    SET\n" +
            "        username = p_username,\n" +
            "        email_address = p_email_address,\n" +
            "        password = crypt(p_password, gen_salt('bf', 8)),\n" +
            "        updated_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_user_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String deleteUser = "CREATE OR REPLACE FUNCTION public.delete_user(p_user_id UUID)\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE \"user\"\n" +
            "    SET\n" +
            "        deleted_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_user_id;\n" +
            "END;\n" +
            "$function$;\n";
}
