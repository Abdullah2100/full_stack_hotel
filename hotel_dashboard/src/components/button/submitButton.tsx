import { Button, CircularProgress } from "@mui/material";
import { enStatus } from "../../module/enState";

interface submitButtonProps {
    placeHolder: string;
    onSubmit: () => Promise<void>;
    buttonStatus?: enStatus | undefined;
    style?: string | undefined
}

const SubmitButton = (
    {
        onSubmit,
        placeHolder,
        buttonStatus = enStatus.none,
        style = undefined
    }: submitButtonProps) => {
    return (<button
        onClick={buttonStatus !== enStatus.loading ? onSubmit : undefined}
        color="primary"
        className={style}
//        className={}
 //sx={{backgroundColor:'blue'}}
    >
        {
            buttonStatus === enStatus.loading ?
                <CircularProgress sx={{ color: 'white' }} size={10} className="mt-1" />
                : placeHolder
        }
    </button>)

}
export default SubmitButton;