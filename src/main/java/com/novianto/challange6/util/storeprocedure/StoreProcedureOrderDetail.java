package com.novianto.challange6.util.storeprocedure;

import org.springframework.stereotype.Component;

@Component
public class StoreProcedureOrderDetail {

    public String getAllOrderDetail = "CREATE OR REPLACE FUNCTION public.get_all_order_details()\n" +
            "RETURNS TABLE (\n" +
            "    id UUID,\n" +
            "    quantity INTEGER,\n" +
            "    total_price DOUBLE PRECISION,\n" +
            "    order_id UUID,\n" +
            "    product_id UUID,\n" +
            "    created_date TIMESTAMPTZ,\n" +
            "    updated_date TIMESTAMPTZ,\n" +
            "    deleted_date TIMESTAMPTZ\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        od.id,\n" +
            "        od.quantity,\n" +
            "        od.total_price,\n" +
            "        od.order_id,\n" +
            "        od.product_id,\n" +
            "        od.created_date,\n" +
            "        od.updated_date,\n" +
            "        od.deleted_date\n" +
            "    FROM\n" +
            "        orderdetail od;\n" +
            "END;\n" +
            "$function$;\n";

    public String getOrderDetailById = "CREATE OR REPLACE FUNCTION public.get_order_detail_by_id(p_order_detail_id UUID)\n" +
            "RETURNS TABLE (\n" +
            "    id UUID,\n" +
            "    quantity INTEGER,\n" +
            "    total_price DOUBLE PRECISION,\n" +
            "    order_id UUID,\n" +
            "    product_id UUID,\n" +
            "    created_date TIMESTAMPTZ,\n" +
            "    updated_date TIMESTAMPTZ,\n" +
            "    deleted_date TIMESTAMPTZ\n" +
            ")\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    RETURN QUERY\n" +
            "    SELECT\n" +
            "        od.id,\n" +
            "        od.quantity,\n" +
            "        od.total_price,\n" +
            "        od.order_id,\n" +
            "        od.product_id,\n" +
            "        od.created_date,\n" +
            "        od.updated_date,\n" +
            "        od.deleted_date\n" +
            "    FROM\n" +
            "        orderdetail od\n" +
            "    WHERE\n" +
            "        od.id = p_order_detail_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String insertOrderDetail = "CREATE OR REPLACE FUNCTION public.insert_order_detail(\n" +
            "    p_quantity INTEGER,\n" +
            "    p_total_price DOUBLE PRECISION,\n" +
            "    p_order_id UUID,\n" +
            "    p_product_id UUID\n" +
            ")\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    INSERT INTO orderdetail (quantity, total_price, order_id, product_id, created_date, updated_date, deleted_date)\n" +
            "    VALUES (p_quantity, p_total_price, p_order_id, p_product_id, NOW(), NOW(), NULL);\n" +
            "END;\n" +
            "$function$;\n";

    public String updateOrderDetail = "CREATE OR REPLACE FUNCTION public.update_order_detail(\n" +
            "    p_order_detail_id UUID,\n" +
            "    p_quantity INTEGER,\n" +
            "    p_total_price DOUBLE PRECISION,\n" +
            "    p_order_id UUID,\n" +
            "    p_product_id UUID\n" +
            ")\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE orderdetail\n" +
            "    SET\n" +
            "        quantity = p_quantity,\n" +
            "        total_price = p_total_price,\n" +
            "        order_id = p_order_id,\n" +
            "        product_id = p_product_id,\n" +
            "        updated_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_order_detail_id;\n" +
            "END;\n" +
            "$function$;\n";

    public String deleteOrderDetail = "CREATE OR REPLACE FUNCTION public.delete_order_detail(p_order_detail_id UUID)\n" +
            "RETURNS VOID\n" +
            "LANGUAGE plpgsql\n" +
            "AS $function$\n" +
            "BEGIN\n" +
            "    UPDATE orderdetail\n" +
            "    SET\n" +
            "        deleted_date = NOW()\n" +
            "    WHERE\n" +
            "        id = p_order_detail_id;\n" +
            "END;\n" +
            "$function$;\n";
}
