package com.novianto.challange6.util.storeprocedure;

import org.springframework.stereotype.Component;

@Component
public class StoreProcedureMerchant {
    public String getAllMerchant = "CREATE OR REPLACE FUNCTION public.get_all_merchant()\n" +
            " RETURNS TABLE(merchant_id uuid, merchant_name character varying, merchant_location character varying, is_open boolean)\n" +
            " LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        m.id AS merchant_id,\n" +
            "        m.merchant_name,\n" +
            "        m.merchant_location,\n" +
            "        m.open AS is_open\n" +
            "    FROM\n" +
            "        merchants AS m;\n" +
            "END;\n" +
            "$function$\n" +
            ";\n";

    public String getMerchantById = "CREATE OR REPLACE FUNCTION public.get_id_merchant(p_merchant_id uuid)\n" +
            " RETURNS TABLE(merchant_id uuid, merchant_name character varying, merchant_location character varying, is_open boolean)\n" +
            " LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        m.id AS merchant_id,\n" +
            "        m.merchant_name,\n" +
            "        m.merchant_location,\n" +
            "        m.open AS is_open\n" +
            "    FROM\n" +
            "        merchants AS m\n" +
            "    WHERE\n" +
            "        m.id = p_merchant_id;\n" +
            "END;\n" +
            "$function$\n" +
            ";\n";

    public String insertMerchant = "CREATE OR REPLACE FUNCTION public.add_merchant(\n" +
            "    p_merchant_name character varying,\n" +
            "    p_merchant_location character varying,\n" +
            "    p_open boolean\n" +
            ")\n" +
            "RETURNS uuid\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "DECLARE\n" +
            "    merchant_id UUID;\n" +
            "BEGIN\n" +
            "    merchant_id := gen_random_uuid();\n" +
            "\n" +
            "    INSERT INTO merchants (id, merchant_name, merchant_location, open, created_date, updated_date)\n" +
            "    VALUES (merchant_id, p_merchant_name, p_merchant_location, p_open, NOW(), NOW());\n" +
            "\n" +
            "    RETURN merchant_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String updateMerchant = "CREATE OR REPLACE FUNCTION public.update_merchant(\n" +
            "    p_merchant_id uuid,\n" +
            "    p_merchant_name character varying,\n" +
            "    p_merchant_location character varying,\n" +
            "    p_open boolean\n" +
            ")\n" +
            "RETURNS void\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE merchants\n" +
            "    SET\n" +
            "        merchant_name = p_merchant_name,\n" +
            "        merchant_location = p_merchant_location,\n" +
            "        open = p_open,\n" +
            "        updated_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_merchant_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String deleteMerchant = "CREATE OR REPLACE FUNCTION public.delete_merchant(\n" +
            "    p_merchant_id uuid\n" +
            ")\n" +
            "RETURNS void\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE merchants\n" +
            "    SET\n" +
            "        deleted_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_merchant_id;\n" +
            "\n" +
            "    -- Hapus data dari tabel\n" +
            "    DELETE FROM merchants\n" +
            "    WHERE\n" +
            "        id = p_merchant_id;\n" +
            "END;\n" +
            "$function$;\n";
}
