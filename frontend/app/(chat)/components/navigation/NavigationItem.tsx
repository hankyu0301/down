import React from "react";
import Image from "next/image";
import { useParams, useRouter } from "next/navigation";
import ActionTooltip from "../ActionTooltip";

import { cn } from "@/lib/cn";

interface NavigationItemProps {
  id: number;
  name: string;
}

const NavigationItem = ({ id, name }: NavigationItemProps) => {
  return (
    <ActionTooltip side="right" align="center" label={name}>
      <button className="group relative flex items-center">
        <div
          className={cn(
            "absolute left-0 bg-primary rounded-r-full transition-all w-[4px]"
          )}
        />
        <div
          className={cn(
            "relative group flex mx-3 h-[48px] w-[48px] rounded-[24px] group-hover:rounded-[16px] transition-all overflow-hidden"
          )}
        ></div>
      </button>
    </ActionTooltip>
  );
};

export default NavigationItem;
