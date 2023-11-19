package com.novianto.challange6.util.storeprocedure;

import org.springframework.stereotype.Component;

@Component
public class StoreProcedureProduct {

    public String getAllProduct = "CREATE OR REPLACE FUNCTION public.get_all_products()\n" +
            "RETURNS TABLE (\n" +
            "    id UUID,\n" +
            "    product_name character varying,\n" +
            "    price DOUBLE PRECISION,\n" +
            "    merchant_id UUID,\n" +
            "    created_date TIMESTAMPTZ,\n" +
            "    updated_date TIMESTAMPTZ,\n" +
            "    deleted_date TIMESTAMPTZ\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        p.id,\n" +
            "        p.product_name,\n" +
            "        p.price,\n" +
            "        p.merchant_id,\n" +
            "        p.created_date,\n" +
            "        p.updated_date,\n" +
            "        p.deleted_date\n" +
            "    FROM\n" +
            "        product p;\n" +
            "END;\n" +
            "$function$;\n";

    public String getProductById = "CREATE OR REPLACE FUNCTION public.get_product_by_id(p_product_id UUID)\n" +
            "RETURNS TABLE (\n" +
            "    id UUID,\n" +
            "    product_name character varying,\n" +
            "    price DOUBLE PRECISION,\n" +
            "    merchant_id UUID,\n" +
            "    created_date TIMESTAMPTZ,\n" +
            "    updated_date TIMESTAMPTZ,\n" +
            "    deleted_date TIMESTAMPTZ\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        p.id,\n" +
            "        p.product_name,\n" +
            "        p.price,\n" +
            "        p.merchant_id,\n" +
            "        p.created_date,\n" +
            "        p.updated_date,\n" +
            "        p.deleted_date\n" +
            "    FROM\n" +
            "        product p\n" +
            "    WHERE\n" +
            "        p.id = p_product_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String insertProduct = "CREATE OR REPLACE FUNCTION public.insert_product(\n" +
            "    p_product_name character varying,\n" +
            "    p_price DOUBLE PRECISION,\n" +
            "    p_merchant_id UUID\n" +
            ")\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    INSERT INTO product (product_name, price, merchant_id, created_date, updated_date, deleted_date)\n" +
            "    VALUES (p_product_name, p_price, p_merchant_id, NOW(), NOW(), NULL);\n" +
            "END;\n" +
            "$function$;\n";

    public String updateProduct = "CREATE OR REPLACE FUNCTION public.update_product(\n" +
            "    p_product_id UUID,\n" +
            "    p_product_name character varying,\n" +
            "    p_price DOUBLE PRECISION,\n" +
            "    p_merchant_id UUID\n" +
            ")\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE product\n" +
            "    SET\n" +
            "        product_name = p_product_name,\n" +
            "        price = p_price,\n" +
            "        merchant_id = p_merchant_id,\n" +
            "        updated_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_product_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String deleteProduct = "CREATE OR REPLACE FUNCTION public.delete_product(p_product_id UUID)\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE product\n" +
            "    SET\n" +
            "        deleted_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_product_id;\n" +
            "END;\n" +
            "$function$;\n";
}
