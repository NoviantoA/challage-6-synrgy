package com.novianto.challange6.util.storeprocedure;

import org.springframework.stereotype.Component;

@Component
public class StoreProcedureOrder {

    public String getAllOrder = "CREATE OR REPLACE FUNCTION public.get_all_order()\n" +
            "RETURNS TABLE (\n" +
            "    id uuid,\n" +
            "    order_time date,\n" +
            "    destination_address character varying,\n" +
            "    completed boolean,\n" +
            "    user_id uuid,\n" +
            "    created_date timestamptz,\n" +
            "    updated_date timestamptz,\n" +
            "    deleted_date timestamptz\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        od.id,\n" +
            "        od.order_time,\n" +
            "        od.destination_address,\n" +
            "        od.completed,\n" +
            "        od.user_id,\n" +
            "        od.created_date,\n" +
            "        od.updated_date,\n" +
            "        od.deleted_date\n" +
            "    FROM\n" +
            "        order od;\n" +
            "END;\n" +
            "$function$;\n";

    public String getOrderById = "CREATE OR REPLACE FUNCTION public.get_order_by_id(p_order_id uuid)\n" +
            "RETURNS TABLE (\n" +
            "    id uuid,\n" +
            "    order_time date,\n" +
            "    destination_address character varying,\n" +
            "    completed boolean,\n" +
            "    user_id uuid,\n" +
            "    created_date timestamptz,\n" +
            "    updated_date timestamptz,\n" +
            "    deleted_date timestamptz\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        od.id,\n" +
            "        od.order_time,\n" +
            "        od.destination_address,\n" +
            "        od.completed,\n" +
            "        od.user_id,\n" +
            "        od.created_date,\n" +
            "        od.updated_date,\n" +
            "        od.deleted_date\n" +
            "    FROM\n" +
            "        order od\n" +
            "    WHERE\n" +
            "        od.id = p_order_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String insertOrder = "CREATE OR REPLACE FUNCTION public.insert_order(\n" +
            "    p_order_time date,\n" +
            "    p_destination_address character varying,\n" +
            "    p_completed boolean,\n" +
            "    p_user_id uuid\n" +
            ")\n" +
            "RETURNS void\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    INSERT INTO order (order_time, destination_address, completed, user_id, created_date, updated_date, deleted_date)\n" +
            "    VALUES (p_order_time, p_destination_address, p_completed, p_user_id, NOW(), NOW(), NULL);\n" +
            "END;\n" +
            "$function$;\n";

    public String updateOrder = "CREATE OR REPLACE FUNCTION public.update_order(\n" +
            "    p_order_id uuid,\n" +
            "    p_order_time date,\n" +
            "    p_destination_address character varying,\n" +
            "    p_completed boolean\n" +
            ")\n" +
            "RETURNS void\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE order\n" +
            "    SET\n" +
            "        order_time = p_order_time,\n" +
            "        destination_address = p_destination_address,\n" +
            "        completed = p_completed,\n" +
            "        updated_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_order_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String deleteOrder = "CREATE OR REPLACE FUNCTION public.delete_order(p_order_id uuid)\n" +
            "RETURNS void\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE order\n" +
            "    SET\n" +
            "        deleted_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_order_id;\n" +
            "END;\n" +
            "$function$;\n";
}
